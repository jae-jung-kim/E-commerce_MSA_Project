package com.example.userservice.member.service;


import com.example.userservice.exception.BusinessLogicException;
import com.example.userservice.exception.ExceptionCode;
import com.example.userservice.member.dto.MemberForm;
import com.example.userservice.member.entity.Member;
import com.example.userservice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //@RequiredArgsConstructor 어노테이션으로 인해 생략가능
//  public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
//      this.memberRepository = memberRepository;
//      this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//  }

    public Member createMember(MemberForm memberDto) {
        //memberEntity생성
        Member member = memberDto.toEntity();
        //name과 email이 존재(중복)하는지 확인
        verifyExistsName(member.getName());
        verifyExistsEmail(member.getEmail());
        member.setPassword(bCryptPasswordEncoder.encode(memberDto.getPassword()));
        member.setRole("USER");
        member.setUseYn(Member.UseYn.Y);

        // MemberEntity 저장
        return memberRepository.save(member);
    }

    public Member updateMember(Long memberId, MemberForm dto) {
        // 1. DTO -> 엔티티 변환하기
        Member member = dto.toEntity();
        // 2. 타깃 조회하기
        Member target = findVerifiedmember(memberId);
        log.info(member.toString());
        // 3. 정보 업데이트
        Optional.ofNullable(member.getEmail())
                .ifPresent(target::setEmail);
        Optional.ofNullable(member.getAddress())
                .ifPresent(target::setAddress);
        Optional.ofNullable(member.getPassword())
                .ifPresent(password-> {
                    target.setPassword(bCryptPasswordEncoder.encode(password));
                }); // 업데이트 할때도 패스워드가 암호화
        Optional.ofNullable(member.getName())
                .ifPresent(target::setName);
        Optional.ofNullable(member.getPhoneNumber())
                .ifPresent(target::setPhoneNumber);
        verifyExistsName(member.getName()); // 업데이트할때 닉네임 중복확인
        log.info(target.toString());
        return memberRepository.save(target);
    }

    public void deleteMember(Long id) {
        // 1. 대상 찾기
        Member target = memberRepository.findById(id).orElseThrow(()->new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        // 2. 대상 삭제하기(softdelete)
        target.setUseYn(Member.UseYn.N);

        // 3. 대상 refreshToken삭제하기
        memberRepository.save(target);
    }

    //닉네임 중복 하는지 확인
    public void verifyExistsName(String nickname){
        Optional<Member> member = memberRepository.findByNameAndUseYn(nickname, Member.UseYn.Y);
        if(member.isPresent()){
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }

    //이메일 중복 하는지 확인
    public void verifyExistsEmail(String email){
        Optional<Member> member = memberRepository.findByEmailAndUseYn(email, Member.UseYn.Y);
        if(member.isPresent()){
            throw new BusinessLogicException(ExceptionCode.EMAIL_EXISTS);
        }
    }

    //멤버가 존재하는지, 회원상태가 Y인 상태인지 확인
    public Member findVerifiedmember(long memberId){
        Optional<Member> optionalMember = memberRepository.findByIdAndUseYn(memberId, Member.UseYn.Y);

        Member findMember = optionalMember.orElseThrow(()->new BusinessLogicException((ExceptionCode.MEMBER_NOT_FOUND)));

        return findMember;
    }
}

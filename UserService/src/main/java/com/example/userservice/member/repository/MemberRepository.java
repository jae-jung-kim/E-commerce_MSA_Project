package com.example.userservice.member.repository;

import com.example.userservice.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByName(String username);
    //username을 받아 DB 테이블에서 회원을 조회하는 메소드 작성
    Member findByName(String username);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByIdAndUseYn(Long memberId, Member.UseYn useYn);

    Optional<Member> findByNameAndUseYn(String Name, Member.UseYn useYn);
    Optional<Member> findByEmailAndUseYn(String Email, Member.UseYn useYn);



}

package com.example.userservice.security.service;

import com.example.userservice.member.entity.Member;
import com.example.userservice.member.repository.MemberRepository;
import com.example.userservice.security.dto.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member userData = memberRepository.findByName(username);

        if(userData!=null){
            return new CustomUserDetails(userData);
        }
        return null;
    }
}

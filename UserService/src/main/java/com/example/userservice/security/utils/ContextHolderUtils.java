package com.example.userservice.security.utils;

import com.example.userservice.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContextHolderUtils {

    private final MemberRepository memberRepository;

    public ContextHolderUtils(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Object getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getPrincipal();
        } else {
            return null;
        }
    }
}

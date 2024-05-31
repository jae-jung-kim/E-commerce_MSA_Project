package com.example.userservice.member.dto;

import com.example.userservice.member.entity.Member;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class MemberForm {

    private String name;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;

    public Member toEntity(){
        return new Member(name, email, password, address, phoneNumber);
    }
}

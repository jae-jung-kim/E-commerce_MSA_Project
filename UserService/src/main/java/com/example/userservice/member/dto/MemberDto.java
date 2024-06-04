package com.example.userservice.member.dto;

import com.example.userservice.member.entity.Member;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class MemberDto {

    private String name;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;

    private List<ResponseOrder> orders;

    public Member toEntity(){
        return new Member(name, email, password, address, phoneNumber);
    }
}

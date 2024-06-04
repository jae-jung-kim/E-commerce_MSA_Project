package com.example.userservice.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUserDto {
    private String name;
    private String email;
    private String address;
    private String phoneNumber;

    private List<ResponseOrder> orders;
}

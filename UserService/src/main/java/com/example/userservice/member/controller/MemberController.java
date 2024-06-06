package com.example.userservice.member.controller;

import com.example.userservice.config.AppConfig;
import com.example.userservice.member.dto.MemberDto;
import com.example.userservice.member.dto.ResponseUserDto;
import com.example.userservice.member.entity.Member;
import com.example.userservice.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final Environment env;
    private final AppConfig appConfig;
    public MemberController(MemberService memberService, Environment env, AppConfig appConfig) {
        this.memberService = memberService;
        this.env = env;
        this.appConfig = appConfig;
    }

    @PostMapping("/create")
    public ResponseEntity<Member> create(@RequestBody MemberDto MemberDto){
        Member createdMember = memberService.createMember(MemberDto);
        return (createdMember != null) ?
                ResponseEntity.status(HttpStatus.CREATED).body(createdMember) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Member> update(@PathVariable Long id, @RequestBody MemberDto dto) {
        Member updated = memberService.updateMember(id, dto);
        return (updated != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updated) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDto> getMember(@PathVariable Long id){
        Member response = memberService.findVerifiedmember(id);
        ResponseUserDto ResponseMemberDto = appConfig.modelMapper().map(response, ResponseUserDto.class);
        return (ResponseMemberDto != null) ?
                ResponseEntity.status(HttpStatus.OK).body(ResponseMemberDto) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResponseUserDto>> getMembers(){
        Iterable<Member> memberList = memberService.getMemberByAll();

        List<ResponseUserDto> result = new ArrayList<>();
        memberList.forEach(v -> {
            result.add(appConfig.modelMapper().map(v, ResponseUserDto.class));
        });
        return (!result.isEmpty()) ?
                ResponseEntity.status(HttpStatus.OK).body(result) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("test")
    public String mainP(){ //test
        return String.format("user Controller Port %s", env.getProperty("local.server.port"));
    }
}

package com.example.userservice.member.controller;

import com.example.userservice.member.dto.MemberForm;
import com.example.userservice.member.entity.Member;
import com.example.userservice.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/create")
    public ResponseEntity<Member> create(@RequestBody MemberForm MemberDto){
        Member createdMember = memberService.createMember(MemberDto);
        return (createdMember != null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdMember) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Member> update(@PathVariable Long id, @RequestBody MemberForm dto) {
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
}
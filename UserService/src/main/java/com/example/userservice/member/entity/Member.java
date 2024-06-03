package com.example.userservice.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Temporal(TemporalType.TIMESTAMP) //JPA에서 날짜 및 시간 타입 매핑할 때 사용
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    private UseYn useYn;
    public enum UseYn{
        Y, //회원가입상태
        N //회원탈퇴상태
    }

    public Member(String name, String email, String password, String address, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    @PrePersist //영구 저장소에 저장되기 전에 실행되는 메서드
    protected void onCreate(){
        createdAt = new Date();
    }

    @PreUpdate //업데이트되기 전에 실행되는 메서드
    protected void onUpdate(){
        updatedAt = new Date();
    }
}

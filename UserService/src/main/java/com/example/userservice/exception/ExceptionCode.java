package com.example.userservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
    MEMBER_EXISTS(HttpStatus.BAD_REQUEST, "중복된 닉네임입니다."),
    EMAIL_EXISTS(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    ACCESS_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "권한이 없습니다."),

    WISHLIST_EXISTS(HttpStatus.BAD_REQUEST, "이미 wishList에 등록되었습니다."),
    WISHLIST_NOT_EXISTS(HttpStatus.BAD_REQUEST, "wishList에 등록되어있지 않습니다."),

    AUCTION_INTERNAL_SERVER_ERROR(HttpStatus.BAD_REQUEST,"양수를 입력해주세요.");

    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message){
        this.status = status;
        this.message = message;
    }
}

package com.hotsix.omc.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EMAIL_NOT_EXIST("존재하지 않는 아이디입니다."),
    PASSWORD_NOT_MATCH("패스워드가 일치하지 않습니다."),
    BAD_REQUEST("잘못된 요청입니다."),
    ALREADY_EXIST_USER("이미 존재하는 회원입니다.");
    private final String errorMessage;
}

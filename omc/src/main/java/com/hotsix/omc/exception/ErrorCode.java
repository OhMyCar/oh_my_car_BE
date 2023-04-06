package com.hotsix.omc.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    SELLER_NOT_FOUND(HttpStatus.BAD_REQUEST, "셀러를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String detail;
}

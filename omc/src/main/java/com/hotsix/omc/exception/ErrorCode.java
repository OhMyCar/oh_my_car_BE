package com.hotsix.omc.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EMAIL_NOT_EXIST("존재하지 않는 아이디입니다."),
    PASSWORD_NOT_MATCH("패스워드가 일치하지 않습니다."),
    BAD_REQUEST("잘못된 요청입니다."),
    ALREADY_EXIST_USER("이미 존재하는 회원입니다."),
    SELLER_NOT_FOUND("셀러를 찾을 수 없습니다."),
    ALREADY_REGISTERED_REVIEW("이미 등록된 리뷰가 있습니다."),
    RESERVATION_NOT_MATCH("예약한 업체가 아닙니다."),
    REVIEW_NOT_EXIST("리뷰가 존재하지 않습니다."),
    REVIEW_NOT_MATCH("리뷰 작성자가 다릅니다."),
    STORE_NOT_FOUND("업체를 찾을 수 없습니다."),
    ALREADY_EXIST_STORE("이미 존재하는 업체입니다.");


    private final String errorMessage;

}

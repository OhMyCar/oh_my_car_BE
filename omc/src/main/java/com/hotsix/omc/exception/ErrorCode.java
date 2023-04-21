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
    STORE_NOT_FOUND("업체를 찾을 수 없습니다."),
    FAILED_FIREBASE_INIT("failed firebase initialize. "),
    NOTIFICATION_NOT_FOUND("notification not found."),
    NOTIFICATION_DELETED("notification deleted."),
    FAILED_SEND_MESSAGE("send message is failed."),
    CAN_NOT_FOUND_TOKEN("token not found."),
    NOT_FOUNT_NOTIFICATION("not found notification."),
    NOTIFICATION_RECEIVER_NOT_MATCH("notification receiver doesn't match."),
    UNMATCH_NOTIFICATION_RECEIVER("unmatch notification receiver.");
    private final String errorMessage;

}

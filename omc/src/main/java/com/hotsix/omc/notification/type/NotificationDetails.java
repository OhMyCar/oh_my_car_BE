package com.hotsix.omc.notification.type;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationDetails {
    CAR_INSPECTION_PERIOD("차량 점검 기간입니다."),
    UPDATE_CAR_INFO("차량 정보를 업데이트 해주세요."),
    CHECK_REVIEW_REPLY("내 리뷰에 댓글이 달렸습니다."),
    WRITE_RIVEW("리뷰를 작성해주세요.");



    private final String message;
}
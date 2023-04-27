package com.hotsix.omc.domain.form.notification;

import lombok.*;

public class NotificationOptionForm {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SaveRequest {
        private String fcmToken;
        private boolean notificationPermit;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChangeRequest {
        private boolean notificationPermit;
    }
}

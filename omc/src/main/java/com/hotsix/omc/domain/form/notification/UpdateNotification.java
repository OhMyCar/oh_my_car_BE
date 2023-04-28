package com.hotsix.omc.domain.form.notification;

import lombok.*;

public class UpdateNotification {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private boolean notificationPermit;
    }
}

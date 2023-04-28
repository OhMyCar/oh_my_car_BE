package com.hotsix.omc.domain.form.notification;

import com.hotsix.omc.domain.entity.Notification;
import com.hotsix.omc.domain.entity.type.NotificationDetails;
import com.hotsix.omc.domain.entity.type.NotificationStatus;
import com.hotsix.omc.domain.entity.type.NotificationType;
import lombok.*;

public class FindNotification {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotificationResponse{
        private String fcmToken;
        private NotificationType notificationType;
        private NotificationDetails notificationDetails;
        private NotificationStatus notificationStatus;


        public static NotificationResponse response (Notification notification){
            return NotificationResponse.builder()
                    .notificationType(notification.getNotificationType())
                    .notificationDetails(notification.getNotificationDetails())
                    .notificationStatus(notification.getNotificationStatus())
                    .build();
        }
    }
}

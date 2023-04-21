package com.hotsix.omc.notification.domain;

import com.hotsix.omc.notification.type.NotificationDetails;
import com.hotsix.omc.notification.type.NotificationStatus;
import com.hotsix.omc.notification.type.NotificationType;
import com.hotsix.omc.notification.type.PageType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private String notificationId;
    private NotificationType notificationType;
    private PageType pageType;
    private Long pageId;
    private NotificationDetails notificationDetails;
    private NotificationStatus notificationStatus;
    private LocalDateTime notifiedAt;

    public static NotificationResponse fromEntity(Notification notification){
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .notificationType(notification.getNotificationType())
                .pageType(notification.getPageType())
                .pageId(notification.getPageId())
                .notificationDetails(notification.getNotificationDetails())
                .notificationStatus(notification.getNotificationStatus())
                .notifiedAt(notification.getNotifiedAt())
                .build();
    }
}

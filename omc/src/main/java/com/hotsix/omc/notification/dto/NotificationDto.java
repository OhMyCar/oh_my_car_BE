package com.hotsix.omc.notification.dto;


import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.notification.type.NotificationDetails;
import com.hotsix.omc.notification.type.NotificationStatus;
import com.hotsix.omc.notification.type.NotificationType;
import com.hotsix.omc.notification.type.PageType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {
    private List<String> tokens;
    private Customer customer;
    private NotificationType notificationType;
    private PageType pageType;
    private Long pageId;
    private NotificationDetails notificationDetails;
    private NotificationStatus notificationStatus;
    private LocalDateTime notifiedAt;
}

package com.hotsix.omc.domain.dto;


import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.entity.type.NotificationDetails;
import com.hotsix.omc.domain.entity.type.NotificationStatus;
import com.hotsix.omc.domain.entity.type.NotificationType;
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
    private NotificationDetails notificationDetails;
    private NotificationStatus notificationStatus;
    private LocalDateTime notifiedAt;
}

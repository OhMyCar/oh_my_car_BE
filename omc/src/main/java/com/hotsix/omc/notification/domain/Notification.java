package com.hotsix.omc.notification.domain;

import com.hotsix.omc.domain.entity.BaseEntity;
import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.notification.dto.NotificationDto;
import com.hotsix.omc.notification.type.NotificationDetails;
import com.hotsix.omc.notification.type.NotificationStatus;
import com.hotsix.omc.notification.type.NotificationType;
import com.hotsix.omc.notification.type.PageType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer receiver;
    @Column(unique = true)
    private String notificationId;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    @Enumerated(EnumType.STRING)
    private PageType pageType;
    private Long pageId;
    @Enumerated(EnumType.STRING)
    private NotificationDetails notificationDetails;
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;
    private LocalDateTime notifiedAt;

    public static Notification of(String notificationId, NotificationDto dto){
        return Notification.builder()
                .receiver(dto.getCustomer())
                .notificationId(notificationId)
                .notificationType(dto.getNotificationType())
                .pageType(dto.getPageType())
                .pageId(dto.getPageId())
                .notificationDetails(dto.getNotificationDetails())
                .notificationStatus(dto.getNotificationStatus())
                .notifiedAt(dto.getNotifiedAt())
                .build();
    }
    public void updateStatus(){
        this.notificationStatus = NotificationStatus.READ;
    }
}

package com.hotsix.omc.domain.entity;

import com.hotsix.omc.domain.dto.NotificationDto;
import com.hotsix.omc.domain.entity.type.NotificationDetails;
import com.hotsix.omc.domain.entity.type.NotificationStatus;
import com.hotsix.omc.domain.entity.type.NotificationType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 알람 자체에 대한 테이블
 * 회원과 매핑된 알람의 허용 여부는 NotificationInfo 테이블
 */

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
    private NotificationDetails notificationDetails;
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;
    private LocalDateTime notifiedAt;

    public static Notification from(String notificationId, NotificationDto dto){
        return Notification.builder()
                .receiver(dto.getCustomer())
                .notificationId(notificationId)
                .notificationType(dto.getNotificationType())
                .notificationDetails(dto.getNotificationDetails())
                .notificationStatus(dto.getNotificationStatus())
                .notifiedAt(dto.getNotifiedAt())
                .build();
    }
    public void updateStatus(){
        this.notificationStatus = NotificationStatus.READ;
    }
}

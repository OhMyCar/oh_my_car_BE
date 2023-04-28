package com.hotsix.omc.domain.entity;


import com.hotsix.omc.domain.form.notification.NotificationOptionForm;
import com.hotsix.omc.domain.form.notification.UpdateNotification;
import lombok.*;

import javax.persistence.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NotificationPermit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Customer customer;
    private String fcmToken;
    private boolean notificationPermit;

    public static NotificationPermit from (NotificationOptionForm.SaveRequest request, Customer customer){
        return NotificationPermit.builder()
                .customer(customer)
                .fcmToken(request.getFcmToken())
                .notificationPermit(request.isNotificationPermit())
                .build();
    }

    public void updateNotificationPermit(UpdateNotification.UpdateRequest request, NotificationPermit info){
        info.notificationPermit = request.isNotificationPermit();
    }
}

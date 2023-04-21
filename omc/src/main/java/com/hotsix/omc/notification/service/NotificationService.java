package com.hotsix.omc.notification.service;

import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.exception.OmcException;
import com.hotsix.omc.notification.domain.Notification;
import com.hotsix.omc.notification.domain.NotificationResponse;
import com.hotsix.omc.notification.repository.NotificationRepository;
import com.hotsix.omc.notification.type.NotificationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.hotsix.omc.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public Slice<NotificationResponse> getNotification(Customer customer, Pageable pageable){
        return notificationRepository
                .findAllByReceiverIdAndNotificationStatusNot(customer.getId(), NotificationStatus.DELETED, pageable)
                .map(NotificationResponse::fromEntity);
    }

    @Transactional
    public String updateNotificationStatusRead(String notificationId, Customer customer){
        Notification notification = notificationRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new OmcException(NOTIFICATION_NOT_FOUND));

        validateUpdateNotification(notification, customer);
        notification.updateStatus();
        return notificationId;
    }

    private void validateUpdateNotification(Notification notification, Customer customer) {
        if (Objects.equals(NotificationStatus.DELETED, notification.getNotificationStatus())){
            throw new OmcException(NOTIFICATION_DELETED);
        }

        if (!Objects.equals(notification.getReceiver().getId(), customer.getId())){
            throw new OmcException(UNMATCH_NOTIFICATION_RECEIVER);
        }
    }

    public String deleteNotification(String notificationId, Customer customer) {
        Notification notification = notificationRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new OmcException(NOT_FOUNT_NOTIFICATION));

        if (!Objects.equals(notification.getReceiver().getId(), customer.getId())){
            throw new OmcException(NOTIFICATION_RECEIVER_NOT_MATCH);
        }
        notificationRepository.deleteById(notification.getId());
        return notificationId;
    }
}

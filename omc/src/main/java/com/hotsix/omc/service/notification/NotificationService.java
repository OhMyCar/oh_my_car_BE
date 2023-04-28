package com.hotsix.omc.service.notification;

import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.entity.Notification;
import com.hotsix.omc.domain.entity.type.NotificationStatus;
import com.hotsix.omc.exception.OmcException;
import com.hotsix.omc.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.hotsix.omc.domain.form.notification.FindNotification.NotificationResponse;
import static com.hotsix.omc.exception.ErrorCode.NOTIFICATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    // 회원이 받을 모든 알람 찾기
    @Transactional(readOnly = true)
    public List<NotificationResponse> findAllNotification(Customer customer) {
        List<Notification> notificationList = notificationRepository.findAllByCustomerId(customer.getId());
        return notificationList.stream()
                .map(NotificationResponse::response)
                .collect(Collectors.toList());
    }

    // 회원의 알람 상태 업데이크
    @Transactional
    public String updateNotificationStatus(String notificationId, Customer customer) {
        if (!notificationRepository.findByNotificationId(notificationId).isPresent()) {
            throw new OmcException(NOTIFICATION_NOT_FOUND);
        }

        Notification notification =
                notificationRepository.findByNotificationId(notificationId).get();
        notification.updateStatus();
        return notificationId;

    }

    // 모든 알람 읽음 처리
    @Transactional
    public Long readAllNotification(Customer customer) {
        List<Notification> notificationList = notificationRepository
                .findAllByCustomerId(customer.getId());

        for (Notification notification : notificationList) {
            notification.setNotificationStatus(NotificationStatus.READ);
        }
        return customer.getId();

    }

    // 개별 알람 삭제
    @Transactional
    public String deleteNotification(String notificationId, Customer customer) {
        if (notificationRepository.findByNotificationId(notificationId).isEmpty()) {
            throw new OmcException(NOTIFICATION_NOT_FOUND);
        }
        Notification notification =
                notificationRepository.findByNotificationId(notificationId).get();
        notificationRepository.deleteById(notification.getId());
        return notificationId;
    }

    // 모든 알람 삭제
    @Transactional
    public Long deleteAllNotification(Customer customer) {
        notificationRepository.deleteAllByCustomerId(customer.getId());
        return customer.getId();
    }
}

package com.hotsix.omc.service.notification.notificationListener;

import com.hotsix.omc.domain.dto.NotificationDto;
import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.entity.NotificationPermit;
import com.hotsix.omc.domain.entity.type.NotificationDetails;
import com.hotsix.omc.domain.entity.type.NotificationType;
import com.hotsix.omc.fcm.FirebaseCloudMessageApi;
import com.hotsix.omc.repository.NotificationPermitRepository;
import com.hotsix.omc.service.notification.event.InspectionEvent;
import com.hotsix.omc.service.notification.event.ReservationEvent;
import com.hotsix.omc.service.notification.event.ReviewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Async("asyncNotification")
@RequiredArgsConstructor
@Slf4j
public class Listener {

    // 자동차 정보 마지막 업데이트 기준 한달 뒤 알람
    // 예약 취소시 알람
    // 예약 확정시 알람
    // 예약 서비스 종료 후 리뷰 작성 요청 알람
    // 내 리뷰에 답글 알람
    private final FirebaseCloudMessageApi firebaseCloudMessageApi;
    private final NotificationPermitRepository notificationPermitRepository;


    @EventListener
    public void reservationConfirmEvent(ReservationEvent reservationEvent) {
        try {
            Customer receiver = reservationEvent.getReceiver();

            Optional<NotificationPermit> optionalNotificationPermit =
                    notificationPermitRepository.findByCustomerId(receiver.getId());

            if (optionalNotificationPermit.isEmpty()) {
                log.error(receiver.getNickname() + "님의 알람정보가 없습니다.");
            }

            NotificationPermit notificationPermit = optionalNotificationPermit.get();
            if (notificationPermit.isNotificationPermit()) {
                firebaseCloudMessageApi.sendNotification(
                        NotificationDto.from(
                                notificationPermit.getFcmToken(),
                                receiver, NotificationType.RESERVATION,
                                NotificationDetails.RESERVATION_CONFIRMED)
                );
            }
        } catch (Exception e) {
            log.error("confirm 알람 {}", e.getMessage(), e);
        }
    }

    @EventListener
    public void reservationCancelEvent(ReservationEvent reservationEvent) {
        try {
            Customer receiver = reservationEvent.getReceiver();

            Optional<NotificationPermit> optionalNotificationPermit =
                    notificationPermitRepository.findByCustomerId(receiver.getId());

            if (optionalNotificationPermit.isEmpty()) {
                log.error(receiver.getNickname() + "님의 알람정보가 없습니다.");
            }

            NotificationPermit notificationPermit = optionalNotificationPermit.get();
            if (notificationPermit.isNotificationPermit()) {
                firebaseCloudMessageApi.sendNotification(
                        NotificationDto.from(
                                notificationPermit.getFcmToken(),
                                receiver, NotificationType.RESERVATION,
                                NotificationDetails.RESERVATION_CANCELED)
                );
            }
        } catch (Exception e) {
            log.error("cancel 알람 {}", e.getMessage(), e);
        }
    }

    @EventListener
    public void replyReviewEvent(ReviewEvent reviewEvent) {
        try {
            Customer receiver = reviewEvent.getReceiver();
            Optional<NotificationPermit> optionalNotificationPermit =
                    notificationPermitRepository.findByCustomerId(receiver.getId());

            if (optionalNotificationPermit.isEmpty()) {
                log.error(receiver.getNickname() + "님의 알람정보가 없습니다.");
            }

            NotificationPermit notificationPermit = optionalNotificationPermit.get();
            if (notificationPermit.isNotificationPermit()) {
                firebaseCloudMessageApi.sendNotification(
                        NotificationDto.from(
                                notificationPermit.getFcmToken(),
                                receiver, NotificationType.REVIEW,
                                NotificationDetails.CHECK_REVIEW_REPLY)
                );
            }
        } catch (Exception e) {
            log.error("cancel 알람 {}", e.getMessage(), e);
        }
    }

    @EventListener
    public void carUpdateRequest(InspectionEvent inspectionEvent) {
        try {
            Customer receiver = inspectionEvent.getCustomer();
            Optional<NotificationPermit> optionalNotificationPermit =
                    notificationPermitRepository.findByCustomerId(receiver.getId());
            if (optionalNotificationPermit.isEmpty()) {
                log.error(receiver.getNickname() + "님의 알람정보가 없습니다.");
            }

            LocalDateTime now = LocalDateTime.now();
            NotificationPermit notificationPermit = optionalNotificationPermit.get();
            if (notificationPermit.isNotificationPermit()
                    && inspectionEvent.getLastModifiedAt() == now.plusDays(30)) {
                firebaseCloudMessageApi.sendNotification(
                        NotificationDto.from(
                                notificationPermit.getFcmToken(),
                                receiver, NotificationType.CAR_INSPECTION,
                                NotificationDetails.UPDATE_CAR_INFO)
                );
            }
        } catch (Exception e) {
            log.error("cancel 알람 {}", e.getMessage(), e);
        }
    }
}


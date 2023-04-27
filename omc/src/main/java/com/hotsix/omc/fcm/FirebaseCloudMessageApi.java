package com.hotsix.omc.fcm;


import com.google.firebase.messaging.*;
import com.hotsix.omc.domain.dto.NotificationDto;
import com.hotsix.omc.exception.OmcException;
import com.hotsix.omc.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.hotsix.omc.exception.ErrorCode.FAILED_SEND_MESSAGE;
import static com.hotsix.omc.fcm.FcmConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseCloudMessageApi {
    private final NotificationRepository notificationRepository;
    private static final String REPLACE_HYPHEN = "-";

    public void sendNotificationByToken(NotificationDto dto){

        for(String token : dto.getTokens()) {
            WebpushConfig webpushConfig = WebpushConfig.builder()
                    .setNotification(new WebpushNotification(dto.getNotificationType().name(),
                            dto.getNotificationDetails().getMessage())).build();

            String now = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            String notificationId = UUID.randomUUID().toString()
                    .replaceAll(REPLACE_HYPHEN, "")
                    .concat(now); //notificationId 와 현재 시간 now 를 연결해줌. ex) fni23nofnirnoin2in202304251411


            // 메세지 작성
            Message message = makeMessage(dto, token, webpushConfig, notificationId);

            // response
            try {
                String response = FirebaseMessaging.getInstance().send(message);
                log.info(response);

                notificationRepository.save(
                        Notification.of(notificationId, dto));
            } catch (FirebaseMessagingException e){
                throw new OmcException(FAILED_SEND_MESSAGE);
            }
        }
    }

    private static Message makeMessage(NotificationDto dto, String token, WebpushConfig webpushConfig, String notificationId) {

        return Message.builder()
                .setToken(token)
                .setWebpushConfig(webpushConfig)
                .putData(NOTIFICATION_ID, notificationId)
                .putData(RECEIVER_NICKNAME, dto.getCustomer().getNickname())
                .putData(NOTIFICATION_TYPE, dto.getNotificationType().name())
                .putData(NOTIFICATION_DETAILS, dto.getNotificationDetails().name())
                .putData(NOTIFIED_AT, dto.getNotifiedAt().toString())
                .build();
    }
}

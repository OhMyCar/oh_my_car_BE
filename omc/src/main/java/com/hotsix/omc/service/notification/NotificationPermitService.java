package com.hotsix.omc.service.notification;

import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.entity.NotificationPermit;
import com.hotsix.omc.exception.ErrorCode;
import com.hotsix.omc.exception.OmcException;
import com.hotsix.omc.repository.NotificationPermitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hotsix.omc.domain.form.notification.NotificationOptionForm.ChangeRequest;
import static com.hotsix.omc.domain.form.notification.NotificationOptionForm.SaveRequest;


// 회원의 알람설정을 저장하는 서비스
@Service
@RequiredArgsConstructor
public class NotificationPermitService {
    private final NotificationPermitRepository notificationPermitRepository;

    // NotificationPermitRepository 에 회원이 없으면 저장

    public String saveNotificationPermission(SaveRequest request, Customer customer){
        Optional<NotificationPermit> optionalNotificationPermit = notificationPermitRepository.findByCustomerId(customer.getId());
        // 조회시 있으면 set
        if (optionalNotificationPermit.isPresent()){
            NotificationPermit notificationPermit = optionalNotificationPermit.get();
            notificationPermit.setFcmToken(request.getFcmToken());
        } // 없으면 save
        else{
            notificationPermitRepository.save(NotificationPermit.from(request, customer));
        }

        return request.getFcmToken();
    }

    // 알람 설정 수정 메서드

    public String changeNotificationOption(ChangeRequest request, Customer customer){
        Optional<NotificationPermit> optionalNotificationPermit = notificationPermitRepository.findByCustomerId(customer.getId());
        if (optionalNotificationPermit.isEmpty()){
            throw new OmcException(ErrorCode.NOTIFICATION_PERMIT_INFO_NOT_EXIST);
        }
        NotificationPermit notificationPermit = optionalNotificationPermit.get();

        notificationPermit.setNotificationPermit(request.isNotificationPermit());
        notificationPermitRepository.save(notificationPermit);
        return notificationPermit.getFcmToken();
    }




}

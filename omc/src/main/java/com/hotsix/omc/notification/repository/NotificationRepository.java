package com.hotsix.omc.notification.repository;

import com.hotsix.omc.notification.domain.Notification;
import com.hotsix.omc.notification.type.NotificationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Slice<Notification> findAllByReceiverIdAndNotificationStatusNot(Long id, NotificationStatus deleted, Pageable pageable);

    Optional<Notification> findByNotificationId(String notificationId);
}

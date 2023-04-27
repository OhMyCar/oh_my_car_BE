package com.hotsix.omc.repository;

import com.hotsix.omc.domain.entity.Notification;
import com.hotsix.omc.domain.entity.type.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByCustomerId(Long customerId);
    List<Notification> findAllByCustomerIdAndNotificationStatus_Unread(Long customerId, NotificationStatus status);
    Optional<Notification> findByNotificationId(String notificationId);
    void deleteById(Long notificationId);
    void deleteAllByCustomerId(Long customerId);

}

package com.hotsix.omc.controller;

import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.form.notification.FindNotification;
import com.hotsix.omc.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<FindNotification.NotificationResponse>> getNotifications(
            @AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(notificationService.findAllNotification(customer));
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<String> updateNotificationStatus(
            @AuthenticationPrincipal Customer customer,
            @PathVariable String notificationId) {
        return ResponseEntity.ok(notificationService.updateNotificationStatus(notificationId, customer));
    }

    @PutMapping
    public ResponseEntity<Long> readAllNotification(
            @AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(notificationService.readAllNotification(customer));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(
            @PathVariable String notificationId,
            @AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(notificationService.deleteNotification(notificationId, customer));
    }

    @DeleteMapping
    public ResponseEntity<Long> deleteAllNotification(@AuthenticationPrincipal Customer customer){
        return ResponseEntity.ok(notificationService.deleteAllNotification(customer));
    }
}

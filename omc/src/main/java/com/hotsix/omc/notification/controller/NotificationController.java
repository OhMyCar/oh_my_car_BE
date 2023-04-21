package com.hotsix.omc.notification.controller;

import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.notification.domain.NotificationResponse;
import com.hotsix.omc.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/notification")
@RequiredArgsConstructor
@RestController
public class NotificationController {
    private final NotificationService notificationService;
    private final static String SORTING_CRITERIA = "notifiedAt";

    @GetMapping
    public ResponseEntity<Slice<NotificationResponse>> getNotification(
            @AuthenticationPrincipal Customer customer,
            @PageableDefault(sort = SORTING_CRITERIA, direction = Sort.Direction.DESC)Pageable pageable) {
        return ResponseEntity.ok(notificationService.getNotification(customer, pageable));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(
            @PathVariable String notificationId,
            @AuthenticationPrincipal Customer customer){
        return ResponseEntity.ok(notificationService.deleteNotification(notificationId, customer));
    }


    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> updateNotificationStatus(
            @PathVariable String notificationId,
            @AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(notificationService.updateNotificationStatusRead(notificationId, customer));
    }
}

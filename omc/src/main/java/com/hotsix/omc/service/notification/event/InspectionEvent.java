package com.hotsix.omc.service.notification.event;


import com.hotsix.omc.domain.entity.Customer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class InspectionEvent {
    private final Customer customer;
    private final String nickname;
    private final LocalDateTime lastModifiedAt;
}

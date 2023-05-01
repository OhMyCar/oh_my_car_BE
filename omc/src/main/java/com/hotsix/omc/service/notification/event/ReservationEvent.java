package com.hotsix.omc.service.notification.event;


import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.entity.Store;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReservationEvent {
    private final Customer receiver;
    private final Store store;
    private final LocalDateTime reservedAt;
}

package com.hotsix.omc.service.notification.event;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class InspectionEvent {
    private final String nickname;
    private final LocalDateTime lastModifiedAt;
}

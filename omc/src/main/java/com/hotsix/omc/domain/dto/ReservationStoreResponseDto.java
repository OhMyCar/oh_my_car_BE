package com.hotsix.omc.domain.dto;

import com.hotsix.omc.domain.entity.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReservationStoreResponseDto {
    private Long customerId;
    private Long storeId;
    private String name;
    private ReservationStatus status;
    private LocalDateTime reservedAt;
}


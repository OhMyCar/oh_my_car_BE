package com.hotsix.omc.domain.dto;

import com.hotsix.omc.domain.entity.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReservationResponseDto {
    private Long customerId;
    private Long storeId;
    private ReservationStatus status;
}


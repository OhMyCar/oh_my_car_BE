package com.hotsix.omc.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ReservationRequestDto {
    private Long customerId;
    private Long storeId;
    private String details;
    private String serviceStartHour;
    private String serviceEndHour;
    private LocalDate serviceDate;
}


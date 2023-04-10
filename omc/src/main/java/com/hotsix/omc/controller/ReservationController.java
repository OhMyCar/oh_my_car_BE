package com.hotsix.omc.controller;

import com.hotsix.omc.domain.entity.ReservationStatus;
import com.hotsix.omc.domain.dto.ReservationRequestDto;
import com.hotsix.omc.domain.dto.ReservationResponseDto;
import com.hotsix.omc.domain.dto.ReservationStoreResponseDto;
import com.hotsix.omc.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reserve")
    public ResponseEntity<ReservationResponseDto> reserve(@RequestBody ReservationRequestDto reservationRequestDto) {
        ReservationResponseDto reservationResponseDto = reservationService.reserve(reservationRequestDto);
        return ResponseEntity.ok(reservationResponseDto);
    }

}
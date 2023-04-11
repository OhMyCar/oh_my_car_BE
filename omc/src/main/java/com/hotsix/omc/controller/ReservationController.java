package com.hotsix.omc.controller;

import com.hotsix.omc.domain.dto.ReservationRequestDto;
import com.hotsix.omc.domain.dto.ReservationResponseDto;
import com.hotsix.omc.domain.dto.ReservationStoreResponseDto;
import com.hotsix.omc.domain.entity.ReservationStatus;
import com.hotsix.omc.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/users/reserve")
    public ResponseEntity<ReservationResponseDto> reserve(@RequestBody ReservationRequestDto reservationRequestDto) {
        ReservationResponseDto reservationResponseDto = reservationService.reserve(reservationRequestDto);
        return ResponseEntity.ok(reservationResponseDto);
    }

    @GetMapping("/users/{customerId}/reserve")
    public ResponseEntity<List<ReservationStoreResponseDto>> getReservation(
            @PathVariable Long customerId,
            @RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime reservedAt,
            @RequestParam(required = false) ReservationStatus status
    ) {
        List<ReservationStoreResponseDto> responseDtoList = reservationService.getReservation(
                customerId,
                reservedAt,
                status);
        return ResponseEntity.ok(responseDtoList);
    }

    @PutMapping("store/{reserveId}/confirm")
    public ResponseEntity<ReservationStoreResponseDto> confirmReservation(@PathVariable Long reserveId) {
        ReservationStoreResponseDto reservationStoreResponseDto = reservationService.confirmReservation(reserveId);
        return ResponseEntity.ok(reservationStoreResponseDto);
    }

    @PutMapping("store/{reserveId}/cancel")
    public ResponseEntity<ReservationStoreResponseDto> cancelReservation(@PathVariable Long reserveId) {
        ReservationStoreResponseDto reservationStoreResponseDto = reservationService.cancelReservation(reserveId);
        return ResponseEntity.ok(reservationStoreResponseDto);
    }

    @GetMapping("store/{storeId}/reservations")
    public ResponseEntity<List<ReservationStoreResponseDto>> getStoreReservations(@PathVariable Long storeId,
                                                                                  @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDateTime).now()}")
                                                                                  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime current,
                                                                                  @RequestParam(required = false, defaultValue = "ALL") ReservationStatus reservationStatus) {
        List<ReservationStoreResponseDto> responseDtoList = reservationService.getStoreReservations(storeId, current, reservationStatus);
        return ResponseEntity.ok(responseDtoList);

    }


}
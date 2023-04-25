package com.hotsix.omc.controller;

import com.hotsix.omc.domain.dto.ReservationStoreResponseDto;
import com.hotsix.omc.domain.entity.ReservationStatus;
import com.hotsix.omc.service.SellerReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class SellerReservationController {

    private final SellerReservationService sellerReservationService;

    @PutMapping("/{reserveId}/confirm")
    public ResponseEntity<ReservationStoreResponseDto> confirmReservation(@PathVariable Long reserveId) {
        ReservationStoreResponseDto reservationStoreResponseDto = sellerReservationService.confirmReservation(reserveId);
        return ResponseEntity.ok(reservationStoreResponseDto);
    }

    @PutMapping("/{reserveId}/cancel")
    public ResponseEntity<ReservationStoreResponseDto> cancelReservation(@PathVariable Long reserveId) {
        ReservationStoreResponseDto reservationStoreResponseDto = sellerReservationService.cancelReservation(reserveId);
        return ResponseEntity.ok(reservationStoreResponseDto);
    }

    @GetMapping("/{storeId}/reservations")
    public ResponseEntity<List<ReservationStoreResponseDto>> getStoreReservations(@PathVariable Long storeId,
                                                                                  @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}")
                                                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to,
                                                                                  @RequestParam(required = false) Optional<ReservationStatus[]> reservationStatus) {
        List<ReservationStoreResponseDto> responseDtoList = sellerReservationService.getStoreReservations(storeId, to, reservationStatus.orElse(ReservationStatus.values()));
        return ResponseEntity.ok(responseDtoList);

    }

    @PutMapping("/{reserveId}/finish")
    public ResponseEntity<ReservationStoreResponseDto> finishReservation(@PathVariable Long reserveId) {
        ReservationStoreResponseDto reservationStoreResponseDto = sellerReservationService.finishReservation(reserveId);
        return ResponseEntity.ok(reservationStoreResponseDto);
    }



}


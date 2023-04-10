package com.hotsix.omc.service;

import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.entity.Reservation;
import com.hotsix.omc.domain.entity.ReservationStatus;
import com.hotsix.omc.domain.entity.Store;
import com.hotsix.omc.domain.dto.ReservationRequestDto;
import com.hotsix.omc.domain.dto.ReservationResponseDto;
import com.hotsix.omc.domain.dto.ReservationStoreResponseDto;
import com.hotsix.omc.repository.CustomerRepository;
import com.hotsix.omc.repository.ReservationRepository;
import com.hotsix.omc.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;

    public ReservationResponseDto reserve(ReservationRequestDto reservationRequestDto) {
        Customer customer = customerRepository.findById(reservationRequestDto.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid customer id"));

        Store store = storeRepository.findById(reservationRequestDto.getStoreId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid store id"));

        Reservation reservation = Reservation.builder()
                .customer(customer)
                .store(store)
                .reservedAt(LocalDateTime.of(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0)))
                .status(ReservationStatus.REQUEST)
                .details(reservationRequestDto.getDetails())
                .serviceStartHour(reservationRequestDto.getServiceStartHour())
                .serviceEndHour(reservationRequestDto.getServiceEndHour())
                .build();

        try {
            Reservation savedReservation = reservationRepository.save(reservation);

            ReservationResponseDto reservationResponseDto = ReservationResponseDto.builder()
                    .customerId(savedReservation.getCustomer().getId())
                    .storeId(savedReservation.getStore().getId())
                    .status(savedReservation.getStatus())
                    .build();

            return reservationResponseDto;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to save reservation information", e);

        }
    }
}
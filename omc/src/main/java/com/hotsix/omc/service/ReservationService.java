package com.hotsix.omc.service;

import com.hotsix.omc.domain.entity.*;
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

    public List<ReservationStoreResponseDto> getReservation(Long customerId,
                                                            LocalDateTime reservedAt,
                                                            ReservationStatus status) {
        // 리포지토리에 존재하는 customerId인지 확인
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user id"));

        // 리포지토리에서 예약정보(기입한 날짜로 부터 최근1년간의 예약정보 불러오기)
        LocalDateTime from = LocalDateTime.of(reservedAt.toLocalDate().minusYears(1), LocalTime.MIN);
        LocalDateTime to = LocalDateTime.of(reservedAt.toLocalDate(), LocalTime.MAX);

        List<Reservation> reservations = reservationRepository.findReservationByCustomerAndReservedAtBetweenAndStatus(
                customer,
                from,
                to,
                status
        );

        // 불러온 예약정보 반복문으로 리스트로 만들기
        List<ReservationStoreResponseDto> storeResponseDtos = new ArrayList<>();
        for (Reservation reservation : reservations) {
            ReservationStoreResponseDto storeResponseDto = ReservationStoreResponseDto.builder()
                    .customerId(customer.getId())
                    .storeId(reservation.getStore().getId())
                    .name(reservation.getStore().getName())
                    .status(reservation.getStatus())
                    .reservedAt(reservation.getReservedAt())
                    .build();
            storeResponseDtos.add(storeResponseDto);
        }
        return storeResponseDtos;
    }

    public ReservationStoreResponseDto confirmReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reservation id"));

        reservation.setStatus(ReservationStatus.CONFIRM);
        reservationRepository.save(reservation);

        return ReservationStoreResponseDto.builder()
                .customerId(reservation.getCustomer().getId())
                .storeId(reservation.getStore().getId())
                .name(reservation.getStore().getName())
                .status(reservation.getStatus())
                .reservedAt(reservation.getReservedAt())
                .build();
    }

    public ReservationStoreResponseDto cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reservation id"));

        reservation.setStatus(ReservationStatus.CANCEL);
        reservationRepository.save(reservation);

        return ReservationStoreResponseDto.builder()
                .customerId(reservation.getCustomer().getId())
                .storeId(reservation.getStore().getId())
                .name(reservation.getStore().getName())
                .status(reservation.getStatus())
                .reservedAt(reservation.getReservedAt())
                .build();
    }

    public List<ReservationStoreResponseDto> getStoreReservations(Long storeId, LocalDateTime current, ReservationStatus reservationStatus) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid store id"));

        LocalDateTime from = LocalDateTime.of(current.toLocalDate().minusYears(1), LocalTime.MIN);
        LocalDateTime to = LocalDateTime.of(current.toLocalDate(), LocalTime.MAX);

        List<Reservation> reservations = reservationRepository.findReservationByStoreAndReservedAtBetweenAndStatus(store, from, to, reservationStatus);

        List<ReservationStoreResponseDto> storeResponseDtoList = new ArrayList<>();
        for (Reservation reservation : reservations) {
            ReservationStoreResponseDto storeResponseDto = ReservationStoreResponseDto.builder()
                    .customerId(reservation.getCustomer().getId())
                    .storeId(reservation.getStore().getId())
                    .name(reservation.getStore().getName())
                    .status(reservation.getStatus())
                    .reservedAt(reservation.getReservedAt())
                    .build();
            storeResponseDtoList.add(storeResponseDto);
        }
        return storeResponseDtoList;
    }
}
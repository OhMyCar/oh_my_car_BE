package com.hotsix.omc.service;

import com.hotsix.omc.domain.entity.*;
import com.hotsix.omc.domain.dto.ReservationRequestDto;
import com.hotsix.omc.domain.dto.ReservationResponseDto;
import com.hotsix.omc.domain.dto.ReservationStoreResponseDto;
import com.hotsix.omc.exception.ErrorCode;
import com.hotsix.omc.exception.OmcException;
import com.hotsix.omc.repository.CarRepository;
import com.hotsix.omc.repository.CustomerRepository;
import com.hotsix.omc.repository.ReservationRepository;
import com.hotsix.omc.repository.StoreRepository;
import com.hotsix.omc.service.notification.event.ReservationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final CarRepository carRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ReservationResponseDto reserve(ReservationRequestDto reservationRequestDto) {
        Customer customer = customerRepository.findById(reservationRequestDto.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid customer id"));

        Store store = storeRepository.findById(reservationRequestDto.getStoreId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid store id"));

        Car car = carRepository.findById(reservationRequestDto.getCarId())
                .orElseThrow(() -> new OmcException(ErrorCode.CAR_NOT_FOUND));

        Reservation reservation = Reservation.builder()
                .customer(customer)
                .store(store)
                .car(car)
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


            applicationEventPublisher.publishEvent(new ReservationEvent(customer, savedReservation.getStore(), savedReservation.getReservedAt()));

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

    public ReservationResponseDto cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));

        // status = CANCEL인 경우(취소불가)
        if (reservation.getStatus() == ReservationStatus.CANCEL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation has already been cancelled");
        }

        // status = FINISH인 경우(취소불가)
        if (reservation.getStatus() == ReservationStatus.FINISH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation has already been finished");
        }

        Store store = reservation.getStore();
        if (store == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation's store is null");
        }

        Customer customer = reservation.getCustomer();
        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation's customer is null");
        }

        // status가 REQUEST or CONFIRM 인 경우(취소가능)
        if (reservation.getStatus() == ReservationStatus.REQUEST || reservation.getStatus() == ReservationStatus.CONFIRM) {
            reservation.setStatus(ReservationStatus.CANCEL);
            reservationRepository.save(reservation);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status for cancel operation");
        }

        ReservationResponseDto reservationResponseDto = ReservationResponseDto.builder()
                .customerId(reservation.getCustomer().getId())
                .storeId(reservation.getStore().getId())
                .status(reservation.getStatus())
                .build();

        return reservationResponseDto;
    }

}
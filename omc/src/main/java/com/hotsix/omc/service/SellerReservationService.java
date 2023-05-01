package com.hotsix.omc.service;

import com.hotsix.omc.domain.dto.ReservationStoreResponseDto;
import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.entity.Reservation;
import com.hotsix.omc.domain.entity.ReservationStatus;
import com.hotsix.omc.domain.entity.Store;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerReservationService {
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CustomerRepository customerRepository;

    public ReservationStoreResponseDto confirmReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reservation id"));

        if (reservation.getStatus() == ReservationStatus.CONFIRM) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation has already been confirmed");
        }

        if (reservation.getStatus() == ReservationStatus.CANCEL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation has been cancelled");
        }

        if (reservation.getStatus() == ReservationStatus.FINISH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation has been finished");
        }


        reservation.setStatus(ReservationStatus.CONFIRM);
        reservationRepository.save(reservation);
        Optional<Customer> optionalCustomer = customerRepository.findCustomerByReservation(reservationId);
        Customer customer = optionalCustomer.get();

        eventPublisher.publishEvent(new ReservationEvent(customer, reservation.getStore(), reservation.getReservedAt()));

        return ReservationStoreResponseDto.builder()
                .customerId(reservation.getCustomer().getId())
                .storeId(reservation.getStore().getId())
                .name(reservation.getStore().getName())
                .status(reservation.getStatus())
                .reservedAt(reservation.getReservedAt())
                .serviceDate(reservation.getServiceDate())
                .serviceStartHour(reservation.getServiceStartHour())
                .serviceEndHour(reservation.getServiceEndHour())
                .build();
    }

    public ReservationStoreResponseDto cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reservation id"));
        if (reservation.getStatus() == ReservationStatus.CANCEL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation has been cancelled");
        }

        if (reservation.getStatus() == ReservationStatus.FINISH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation has been finished");
        }

        reservation.setStatus(ReservationStatus.CANCEL);
        reservationRepository.save(reservation);
        Optional<Customer> optionalCustomer = customerRepository.findCustomerByReservation(reservationId);
        Customer customer = optionalCustomer.get();
        eventPublisher.publishEvent(new ReservationEvent(customer, reservation.getStore(), reservation.getReservedAt()));

        return ReservationStoreResponseDto.builder()
                .customerId(reservation.getCustomer().getId())
                .storeId(reservation.getStore().getId())
                .name(reservation.getStore().getName())
                .status(reservation.getStatus())
                .reservedAt(reservation.getReservedAt())
                .serviceDate(reservation.getServiceDate())
                .serviceStartHour(reservation.getServiceStartHour())
                .serviceEndHour(reservation.getServiceEndHour())
                .build();
    }

    public List<ReservationStoreResponseDto> getStoreReservations(Long storeId, LocalDate to, ReservationStatus[] reservationStatus) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid store id"));

        LocalDate from = to.minusYears(1);

        List<Reservation> reservations = reservationRepository.findReservationByStoreAndServiceDateBetweenAndStatusIn(store, from, to, reservationStatus);
        List<ReservationStoreResponseDto> storeResponseDtoList = new ArrayList<>();
        for (Reservation reservation : reservations) {
            ReservationStoreResponseDto storeResponseDto = ReservationStoreResponseDto.builder()
                    .customerId(reservation.getCustomer().getId())
                    .storeId(reservation.getStore().getId())
                    .name(reservation.getStore().getName())
                    .status(reservation.getStatus())
                    .reservedAt(reservation.getReservedAt())
                    .serviceDate(reservation.getServiceDate())
                    .serviceStartHour(reservation.getServiceStartHour())
                    .serviceEndHour(reservation.getServiceEndHour())
                    .build();
            storeResponseDtoList.add(storeResponseDto);
        }
        return storeResponseDtoList;
    }

    public ReservationStoreResponseDto finishReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reservation id"));
        if (reservation.getStatus() == ReservationStatus.CANCEL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation has been cancelled");
        }

        if (reservation.getStatus() == ReservationStatus.FINISH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation has been finished");
        }

        if (reservation.getStatus() == ReservationStatus.REQUEST) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation has not been confirmed");
        }

        reservation.setStatus(ReservationStatus.FINISH);
        reservationRepository.save(reservation);
        Optional<Customer> optionalCustomer = customerRepository.findCustomerByReservation(reservationId);
        Customer customer = optionalCustomer.get();

        eventPublisher.publishEvent(new ReservationEvent(customer, reservation.getStore(), reservation.getReservedAt()));

        return ReservationStoreResponseDto.builder()
                .customerId(reservation.getCustomer().getId())
                .storeId(reservation.getStore().getId())
                .name(reservation.getStore().getName())
                .status(reservation.getStatus())
                .reservedAt(reservation.getReservedAt())
                .serviceDate(reservation.getServiceDate())
                .serviceStartHour(reservation.getServiceStartHour())
                .serviceEndHour(reservation.getServiceEndHour())
                .build();
    }
}

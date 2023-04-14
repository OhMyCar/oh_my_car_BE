package com.hotsix.omc.service;

import com.hotsix.omc.domain.dto.ReservationStoreResponseDto;
import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.entity.Reservation;
import com.hotsix.omc.domain.entity.ReservationStatus;
import com.hotsix.omc.domain.entity.Store;
import com.hotsix.omc.repository.ReservationRepository;
import com.hotsix.omc.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class SellerReservationServiceTest {
    private SellerReservationService sellerReservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private StoreRepository storeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sellerReservationService = new SellerReservationService(reservationRepository, storeRepository);
    }

    @Test
    public void reservationWithInvalidId() {
        Long invalidId = 1L;
        when(reservationRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            sellerReservationService.confirmReservation(invalidId);
        }, "Invalid reservation id");
    }

    @Test
    public void alreadyConfirmedReservation() {
        Long reservationId = 1L;
        Reservation confirmedReservation = new Reservation();
        confirmedReservation.setId(reservationId);
        confirmedReservation.setStatus(ReservationStatus.CONFIRM);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(confirmedReservation));

        assertThrows(ResponseStatusException.class, () -> {
            sellerReservationService.confirmReservation(reservationId);
        }, "Reservation has already been confirmed");
    }

    @Test
    public void cancelledReservation() {
        Long reservationId = 1L;
        Reservation cancelledReservation = new Reservation();
        cancelledReservation.setId(reservationId);
        cancelledReservation.setStatus(ReservationStatus.CANCEL);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(cancelledReservation));

        assertThrows(ResponseStatusException.class, () -> {
            sellerReservationService.confirmReservation(reservationId);
        }, "Reservation has already been cancelled");
    }

    @Test
    public void finishedReservation() {
        Long reservationId = 1L;
        Reservation finishedReservation = new Reservation();
        finishedReservation.setId(reservationId);
        finishedReservation.setStatus(ReservationStatus.FINISH);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(finishedReservation));

        assertThrows(ResponseStatusException.class, () -> {
            sellerReservationService.confirmReservation(reservationId);
        }, "Reservation has already been finished");
    }

    @Test
    public void confirmReservation() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(ReservationStatus.REQUEST);
        Customer customer = new Customer();
        customer.setId(1L);
        reservation.setCustomer(customer);
        Store store = new Store();
        store.setId(1L);
        store.setName("Test Store");
        reservation.setStore(store);
        reservation.setReservedAt(LocalDateTime.now());
        reservation.setServiceDate(LocalDate.of(2023, 4, 14));
        reservation.setServiceStartHour("10");
        reservation.setServiceEndHour("11");
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));


        ReservationStoreResponseDto responseDto = sellerReservationService.confirmReservation(reservation.getId());

        assertEquals(reservation.getCustomer().getId(), responseDto.getCustomerId());
        assertEquals(reservation.getStore().getId(), responseDto.getStoreId());
        assertEquals(reservation.getStore().getName(), responseDto.getName());
        assertEquals(ReservationStatus.CONFIRM, responseDto.getStatus());
        assertEquals(reservation.getReservedAt(), responseDto.getReservedAt());
        assertEquals(reservation.getServiceDate(), responseDto.getServiceDate());
        assertEquals(reservation.getServiceStartHour(), responseDto.getServiceStartHour());
        assertEquals(reservation.getServiceEndHour(), responseDto.getServiceEndHour());

    }


    @Test
    void cancelReservation() {

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(ReservationStatus.CONFIRM);
        Customer customer = new Customer();
        customer.setId(1L);
        reservation.setCustomer(customer);
        Store store = new Store();
        store.setId(1L);
        store.setName("Test Store");
        reservation.setStore(store);
        reservation.setReservedAt(LocalDateTime.now());
        reservation.setServiceDate(LocalDate.of(2023, 4, 14));
        reservation.setServiceStartHour("10");
        reservation.setServiceEndHour("11");
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));


        ReservationStoreResponseDto responseDto = sellerReservationService.cancelReservation(reservation.getId());


        assertEquals(reservation.getCustomer().getId(), responseDto.getCustomerId());
        assertEquals(reservation.getStore().getId(), responseDto.getStoreId());
        assertEquals(reservation.getStore().getName(), responseDto.getName());
        assertEquals(ReservationStatus.CANCEL, responseDto.getStatus());
        assertEquals(reservation.getReservedAt(), responseDto.getReservedAt());
        assertEquals(reservation.getServiceDate(), responseDto.getServiceDate());
        assertEquals(reservation.getServiceStartHour(), responseDto.getServiceStartHour());
        assertEquals(reservation.getServiceEndHour(), responseDto.getServiceEndHour());
    }


    @Test
    void getStoreReservations() {

        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setStatus(ReservationStatus.REQUEST);
        Customer customer = new Customer();
        customer.setId(1L);
        reservation1.setCustomer(customer);
        Store store = new Store();
        store.setId(1L);
        store.setName("Test Store");
        reservation1.setStore(store);
        reservation1.setReservedAt(LocalDateTime.now());
        reservation1.setServiceDate(LocalDate.of(2023, 4, 10));
        reservation1.setServiceStartHour("10");
        reservation1.setServiceEndHour("11");

        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setStatus(ReservationStatus.CONFIRM);
        Customer customer2 = new Customer();
        customer2.setId(2L);
        reservation2.setCustomer(customer2);
        reservation2.setStore(store);
        reservation2.setReservedAt(LocalDateTime.now());
        reservation2.setServiceDate(LocalDate.of(2023, 4, 14));
        reservation2.setServiceStartHour("10");
        reservation2.setServiceEndHour("11");

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation1);
        reservations.add(reservation2);
        ReservationStatus[] reservationStatus = new ReservationStatus[]{ReservationStatus.CONFIRM};

        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));
        when(reservationRepository.findReservationByStoreAndServiceDateBetweenAndStatusIn(eq(store), any(LocalDate.class), any(LocalDate.class), eq(reservationStatus))).thenReturn(Collections.singletonList(reservation2));

        List<ReservationStoreResponseDto> storeResponseDtoList = sellerReservationService.getStoreReservations(store.getId(), LocalDate.of(2023, 4, 14),  reservationStatus);

        assertNotNull(storeResponseDtoList);
        assertEquals(storeResponseDtoList.size(), 1);

        ReservationStoreResponseDto responseDto = storeResponseDtoList.get(0);
        assertEquals(reservation2.getCustomer().getId(), responseDto.getCustomerId());
        assertEquals(reservation2.getStore().getId(), responseDto.getStoreId());
        assertEquals(reservation2.getStore().getName(), responseDto.getName());
        assertEquals(ReservationStatus.CONFIRM, responseDto.getStatus());
        assertEquals(reservation2.getReservedAt(), responseDto.getReservedAt());
        assertEquals(reservation2.getServiceDate(), responseDto.getServiceDate());
        assertEquals(reservation2.getServiceStartHour(), responseDto.getServiceStartHour());
        assertEquals(reservation2.getServiceEndHour(), responseDto.getServiceEndHour());

    }
}

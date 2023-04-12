package com.hotsix.omc.service.service;

import com.hotsix.omc.domain.dto.ReservationRequestDto;
import com.hotsix.omc.domain.dto.ReservationResponseDto;
import com.hotsix.omc.domain.dto.ReservationStoreResponseDto;
import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.entity.Reservation;
import com.hotsix.omc.domain.entity.ReservationStatus;
import com.hotsix.omc.domain.entity.Store;
import com.hotsix.omc.repository.CustomerRepository;
import com.hotsix.omc.repository.ReservationRepository;
import com.hotsix.omc.repository.StoreRepository;
import com.hotsix.omc.service.ReservationService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    StoreRepository storeRepository;

    @InjectMocks
    ReservationService reservationService;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeMocks() throws Exception {
        mockitoCloseable.close();
    }

    @Nested
    @DisplayName("reserve 메소드")
    class ReserveTest {

        @Test
        @DisplayName("예약 성공")
        void reserveSuccess() {
            // given
            Customer customer = new Customer();
            customer.setId(1L);

            Store store = new Store();
            store.setId(1L);

            ReservationRequestDto reservationRequestDto = ReservationRequestDto.builder()
                    .customerId(1L)
                    .storeId(1L)
                    .details("test")
                    .serviceStartHour("1")
                    .serviceEndHour("2")
                    .build();

            Reservation savedReservation = Reservation.builder()
                    .id(1L)
                    .customer(customer)
                    .store(store)
                    .reservedAt(LocalDateTime.now())
                    .status(ReservationStatus.REQUEST)
                    .details("test")
                    .serviceStartHour("1")
                    .serviceEndHour("2")
                    .build();

            when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
            when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
            when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);

            // when
            ReservationResponseDto result = reservationService.reserve(reservationRequestDto);

            // then
            assertNotNull(result);
            assertEquals(customer.getId(), result.getCustomerId());
            assertEquals(store.getId(), result.getStoreId());
            assertEquals(savedReservation.getStatus(), result.getStatus());
        }

        @Test
        @DisplayName("예약 실패 - 존재하지 않는 customer id 사용")
        void reserveFailInvalidCustomerId() {
            // given
            ReservationRequestDto reservationRequestDto = ReservationRequestDto.builder()
                    .customerId(999L)
                    .storeId(1L)
                    .details("test")
                    .serviceStartHour("1")
                    .serviceEndHour("2")
                    .build();

            when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when, then
            assertThrows(ResponseStatusException.class, () -> reservationService.reserve(reservationRequestDto),
                    "Invalid customer id");
        }

        @Test
        @DisplayName("예약 실패 - 존재하지 않는 store id 사용")
        void reserveFailInvalidStoreId() {
            // given
            ReservationRequestDto reservationRequestDto = ReservationRequestDto.builder()
                    .customerId(1L)
                    .storeId(999L)
                    .details("test")
                    .serviceStartHour("1")
                    .serviceEndHour("2")
                    .build();

            when(customerRepository.findById(anyLong())).thenReturn(Optional.of(new Customer()));
            when(storeRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when, then
            assertThrows(ResponseStatusException.class, () -> reservationService.reserve(reservationRequestDto),
                    "Invalid store id");
        }

        @Test
        @DisplayName("예약 실패 - 예약 정보 저장 실패")
        void reserveFailToSaveReservationInformation() {
            // given
            Customer customer = new Customer();
            customer.setId(1L);

            Store store = new Store();
            store.setId(1L);

            ReservationRequestDto reservationRequestDto = ReservationRequestDto.builder()
                    .customerId(1L)
                    .storeId(1L)
                    .details("test")
                    .serviceStartHour("1")
                    .serviceEndHour("2")
                    .build();

            when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
            when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
            when(reservationRepository.save(any(Reservation.class))).thenThrow(new RuntimeException());

            // when + then
            assertThrows(ResponseStatusException.class, () -> reservationService.reserve(reservationRequestDto));
        }
    }

    @Nested
    @DisplayName("getReservation 메소드")
    class getReservationTest {

        @Test
        @DisplayName("예약 조회 성공")
        void getReservationSuccess() {
            // given
            Long customerId = 1L;
            LocalDateTime reservedAt = LocalDateTime.of(2023, 4, 10, 12, 0);
            LocalDate serviceDate = LocalDate.of(2023, 4, 10);

            Customer customer = Customer.builder()
                    .id(customerId)
                    .build();

            Store store1 = Store.builder()
                    .id(1L)
                    .name("Store1")
                    .build();

            Store store2 = Store.builder()
                    .id(2L)
                    .name("Store2")
                    .build();

            List<Reservation> reservations = new ArrayList<>();
            Reservation reservation1 = Reservation.builder()
                    .id(1L)
                    .customer(customer)
                    .store(store1)
                    .reservedAt(LocalDateTime.of(2023, 4, 9, 10, 0))
                    .status(ReservationStatus.REQUEST)
                    .build();
            Reservation reservation2 = Reservation.builder()
                    .id(2L)
                    .customer(customer)
                    .store(store2)
                    .reservedAt(LocalDateTime.of(2023, 4, 10, 13, 0))
                    .status(ReservationStatus.CONFIRM)
                    .build();
            Reservation reservation3 = Reservation.builder()
                    .id(3L)
                    .customer(customer)
                    .store(store1)
                    .reservedAt(LocalDateTime.of(2023, 4, 11, 10, 0))
                    .status(ReservationStatus.FINISH)
                    .build();
            Reservation reservation4 = Reservation.builder()
                    .id(4L)
                    .customer(customer)
                    .store(store2)
                    .reservedAt(LocalDateTime.of(2023, 4, 12, 13, 0))
                    .status(ReservationStatus.CANCEL)
                    .build();
            reservations.add(reservation1);
            reservations.add(reservation2);
            reservations.add(reservation3);
            reservations.add(reservation4);

            when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
            when(reservationRepository.findReservationByCustomerAndReservedAtBetweenAndStatus(any(), any(), any(), any())).thenReturn(reservations);

            // when
            List<ReservationStoreResponseDto> storeResponseDtos = reservationService.getReservation(customerId, reservedAt, serviceDate,null);

            // then
            assertNotNull(storeResponseDtos);
            assertEquals(4, storeResponseDtos.size());

            ReservationStoreResponseDto storeResponseDto1 = storeResponseDtos.get(0);
            assertEquals(customerId, storeResponseDto1.getCustomerId());
            assertEquals(store1.getId(), storeResponseDto1.getStoreId());
            assertEquals(store1.getName(), storeResponseDto1.getName());
            assertEquals(reservation1.getStatus(), storeResponseDto1.getStatus());
            assertEquals(reservation1.getReservedAt(), storeResponseDto1.getReservedAt());

            ReservationStoreResponseDto storeResponseDto2 = storeResponseDtos.get(1);
            assertEquals(customerId, storeResponseDto2.getCustomerId());
            assertEquals(store2.getId(), storeResponseDto2.getStoreId());
            assertEquals(store2.getName(), storeResponseDto2.getName());
            assertEquals(reservation2.getStatus(), storeResponseDto2.getStatus());
            assertEquals(reservation2.getReservedAt(), storeResponseDto2.getReservedAt());

            ReservationStoreResponseDto storeResponseDto4 = storeResponseDtos.get(3);
            assertEquals(customerId, storeResponseDto4.getCustomerId());
            assertEquals(store2.getId(), storeResponseDto4.getStoreId());
            assertEquals(store2.getName(), storeResponseDto4.getName());
            assertEquals(reservation4.getStatus(), storeResponseDto4.getStatus());
            assertEquals(reservation4.getReservedAt(), storeResponseDto4.getReservedAt());
        }
        @Test
        @DisplayName("예약 조회 실패 - 존재하지 않는 customer id 사용")
        void getReservationFailInvalidCustomerId() {
            // given
            Long customerId = 1L;
            LocalDateTime reservedAt = LocalDateTime.of(2022, 4, 9, 12, 0);
            LocalDate serviceDate = LocalDate.of(2023, 4, 12); 
            ReservationStatus status = ReservationStatus.REQUEST;

            when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

            // when + then
            assertThrows(ResponseStatusException.class, () -> reservationService.getReservation(customerId, reservedAt, serviceDate, status),
                    "Invalid customer id");
        }
    }

    @Nested
    @DisplayName("cancelReservation 메소드")
    class CancelReservationTest {

        @Test
        @DisplayName("예약 취소 성공 - status가 CONFRIM일때")
        void cancelReservationSuccessWhenStatusConfirm() {
            // given
            Long id = 1L;
            Customer customer = Customer.builder().id(1L).build();
            Store store = Store.builder().id(1L).build();
            Reservation reservation = Reservation.builder()
                    .id(id)
                    .status(ReservationStatus.CONFIRM)
                    .customer(customer)
                    .store(store)
                    .build();
            when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

            // when
            ReservationResponseDto reservationResponseDto = reservationService.cancelReservation(id);

            // then
            assertEquals(reservation.getCustomer().getId(), reservationResponseDto.getCustomerId());
            assertEquals(reservation.getStore().getId(), reservationResponseDto.getStoreId());
            assertEquals(ReservationStatus.CANCEL, reservationResponseDto.getStatus());
            verify(reservationRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("예약 취소 성공 - status가 REQUEST일때")
        void cancelReservationSuccessWhenStatusRequest() {
            // given
            Long id = 1L;
            Customer customer = Customer.builder().id(1L).build();
            Store store = Store.builder().id(1L).build();
            Reservation reservation = Reservation.builder()
                    .id(id)
                    .status(ReservationStatus.REQUEST)
                    .customer(customer)
                    .store(store)
                    .build();
            when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

            // when
            ReservationResponseDto reservationResponseDto = reservationService.cancelReservation(id);

            // then
            assertEquals(reservation.getCustomer().getId(), reservationResponseDto.getCustomerId());
            assertEquals(reservation.getStore().getId(), reservationResponseDto.getStoreId());
            assertEquals(ReservationStatus.CANCEL, reservationResponseDto.getStatus());
            verify(reservationRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("예약 취소 실패 - 이미 취소된 예약")
        void cancelReservationFailWhenAlreadyCancelledReservation() {
            // given
            Long reservationId = 1L;
            ReservationStatus status = ReservationStatus.CANCEL;

            Reservation reservation = Reservation.builder()
                    .id(reservationId)
                    .status(status)
                    .build();

            when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

            // when & then
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                reservationService.cancelReservation(reservationId);
            });
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("Reservation has already been cancelled", exception.getReason());
        }

        @Test
        @DisplayName("예약 취소 실패 - 서비스 종료된 예약")
        void cancelReservationFailWhenAlreadyFinishedReservation() {
            // given
            Long reservationId = 1L;
            ReservationStatus status = ReservationStatus.FINISH;

            Reservation reservation = Reservation.builder()
                    .id(reservationId)
                    .status(status)
                    .build();

            when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

            // when & then
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                reservationService.cancelReservation(reservationId);
            });
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("Reservation has already been finished", exception.getReason());
        }

        @Test
        @DisplayName("예약 취소 실패 - 존재하지 않는 예약 취소")
        void cancelFailNonexistentReservation() {
            // given
            Long id = 1L;
            when(reservationRepository.findById(id)).thenReturn(Optional.empty());

            // when, then
            assertThrows(ResponseStatusException.class, () -> reservationService.cancelReservation(id));
            verify(reservationRepository, times(0)).save(any());
        }

        @Test
        @DisplayName("예약 취소 실패 - 이미 취소된 예약 취소")
        void cancelFailAlreadyCancelledReservation() {
            // given
            Long id = 1L;
            Reservation reservation = Reservation.builder()
                    .id(id)
                    .status(ReservationStatus.CANCEL)
                    .build();
            when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

            // when, then
            assertThrows(ResponseStatusException.class, () -> reservationService.cancelReservation(id));
            verify(reservationRepository, times(0)).save(any());
        }

        @Test
        @DisplayName("예약 취소 실패 - 이미 종료된 예약 취소")
        void cancelFailFinishedReservation() {
            // given
            Long id = 1L;
            Reservation reservation = Reservation.builder()
                    .id(id)
                    .status(ReservationStatus.FINISH)
                    .build();
            when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

            // when, then
            assertThrows(ResponseStatusException.class, () -> reservationService.cancelReservation(id));
            verify(reservationRepository, times(0)).save(any());
        }

        @Test
        @DisplayName("예약 취소 실패 - customer가 null일때")
        void cancelReservationFailWithNullCustomer() {
            // given
            Long id = 1L;
            Reservation reservation = Reservation.builder()
                    .id(id)
                    .status(ReservationStatus.CONFIRM)
                    .customer(null)
                    .store(Store.builder().id(1L).build())
                    .build();
            when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

            // when, then
            assertThrows(ResponseStatusException.class, () -> reservationService.cancelReservation(id));
            verify(reservationRepository, times(0)).save(any());
        }

        @Test
        @DisplayName("예약 취소 실패 - store가 null일때")
        void cancelReservationFailWithNullStore() {
            // given
            Long id = 1L;
            Reservation reservation = Reservation.builder()
                    .id(id)
                    .status(ReservationStatus.CONFIRM)
                    .customer(Customer.builder().id(1L).build())
                    .store(null)
                    .build();
            when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

            // when, then
            assertThrows(ResponseStatusException.class, () -> reservationService.cancelReservation(id));
            verify(reservationRepository, times(0)).save(any());
        }
    }
}
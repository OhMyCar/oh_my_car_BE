package com.hotsix.omc.repository;

import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.entity.Reservation;
import com.hotsix.omc.domain.entity.ReservationStatus;
import com.hotsix.omc.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findReservationByCustomerAndReservedAtBetweenAndStatus(
            Customer customer,
            LocalDateTime from,
            LocalDateTime to,
            ReservationStatus status
    );

    List<Reservation> findReservationByStoreAndReservedAtBetweenAndStatusIn(Store store, LocalDateTime from, LocalDateTime to, ReservationStatus[] status);
}
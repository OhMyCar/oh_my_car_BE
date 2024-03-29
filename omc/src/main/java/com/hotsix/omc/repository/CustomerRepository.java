package com.hotsix.omc.repository;

import com.hotsix.omc.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByEmailAuthKey(String uuid);
    Optional<Customer> findCustomerByReservation(Long reservationId);
    Optional<Customer> findCustomerByReview(Long reviewId);
    Optional<Customer> findCustomerByCars(Long carId);

}

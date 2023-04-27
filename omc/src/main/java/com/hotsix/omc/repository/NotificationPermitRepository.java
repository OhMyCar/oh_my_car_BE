package com.hotsix.omc.repository;

import com.hotsix.omc.domain.entity.NotificationPermit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationPermitRepository extends JpaRepository<NotificationPermit, Long> {
    Optional<NotificationPermit> findByCustomerId(Long customerId);

}

package com.hotsix.omc.repository;

import com.hotsix.omc.domain.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityListeners;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long>{
    Optional<Car> findByModifiedAt(Long carId);

}

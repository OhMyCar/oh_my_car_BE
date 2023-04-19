package com.hotsix.omc.repository;

import com.hotsix.omc.domain.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByEmail(String email);
    Optional<Seller> findByEmailAuthKey(String uuid);

}

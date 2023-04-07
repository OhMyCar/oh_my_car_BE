package com.hotsix.omc.repository;

import com.hotsix.omc.domain.entity.Seller;
import com.hotsix.omc.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByIdAndSellerId(Long storeId, Long seller Id);

}

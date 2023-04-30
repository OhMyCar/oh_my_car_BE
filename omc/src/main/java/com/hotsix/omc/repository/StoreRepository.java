package com.hotsix.omc.repository;

import com.hotsix.omc.domain.entity.Seller;
import com.hotsix.omc.domain.entity.Store;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByIdAndSellerId(Long storeId, Long sellerId);

    List<Store> findBySellerId(Long sellerId);

    Optional<Store> findByAddress_Zipcode(String zipcode);

    Optional<Store> findBySellerAndName(Seller seller, String name);
    Optional<Store> findByReviewId(Long reviewId);

    @Query(nativeQuery = true, value = "select *, ST_Distance_Sphere(Point(store.longitude, store.latitude), Point(:currentLng, :currentLat))/1000 as distance from store order by distance limit 5")
    List<Store> findNearestStores(@Param("currentLng") double currentLng, @Param("currentLat") double currentLat);


}

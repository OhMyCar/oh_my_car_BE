package com.hotsix.omc.repository;

import com.hotsix.omc.domain.entity.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

	boolean existsByCustomerIdAndStoreId(Long customerId, Long storeId);

	List<Review> findReviewsByStoreId(Long storeId);

	Optional<Review> findReviewByCustomerIdAndStoreId(Long customerId, Long storeId);
}

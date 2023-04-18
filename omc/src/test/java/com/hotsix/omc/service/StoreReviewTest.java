package com.hotsix.omc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.entity.Review;
import com.hotsix.omc.domain.entity.Store;
import com.hotsix.omc.domain.form.customer.CustomerReviewForm.Response;
import com.hotsix.omc.domain.form.store.StoreReviewForm;
import com.hotsix.omc.repository.CustomerRepository;
import com.hotsix.omc.repository.ReviewRepository;
import com.hotsix.omc.repository.StoreRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StoreReviewTest {

	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private StoreRepository storeRepository;
	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private ReviewService reviewService;

	private AutoCloseable mockitoCloseable;

	@BeforeEach
	void setUp() {
		mockitoCloseable = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void closeMocks() throws Exception {
		mockitoCloseable.close();
	}

	@Test
	@DisplayName("리뷰 답변 성공")
	void success_replyReview() {
		// given
		Review review = new Review();
		review.setId(1L);
		Customer customer = new Customer();
		customer.setId(1L);
		Store store = new Store();
		store.setId(1L);
		review.setCustomer(customer);
		review.setStore(store);
		review.setName("Test");
		review.setComment("만족합니다.");
		review.setRating(4.5);

		StoreReviewForm.Request request = StoreReviewForm.Request.builder()
			.customerId(1L)
			.storeId(1L)
			.reply("이용해주셔서 감사합니다.")
			.build();

		// when
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
		when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
		when(reviewRepository.save(any(Review.class))).thenReturn(review);
		StoreReviewForm.Response response = reviewService.replyReview(1L, request);

		// then
		assertNotNull(response);
		assertEquals(review.getReply(), response.getReply());
	}

	@Test
	@DisplayName ("리뷰 조회")
	void getStoreReview() {
		Review review1 = new Review();
		review1.setId(1L);
		Customer customer = new Customer();
		customer.setId(1L);
		review1.setCustomer(customer);
		Store store = new Store();
		store.setId(1L);
		review1.setStore(store);
		review1.setName("Test");
		review1.setComment("만족합니다.");
		review1.setRating(5.0);

		Review review2 = new Review();
		review2.setId(2L);
		Customer customer2 = new Customer();
		customer2.setId(2L);
		review2.setCustomer(customer2);
		review2.setStore(store);
		review2.setName("Test");
		review2.setComment("불만족합니다.");
		review2.setRating(0.5);

		List<Review> reviews = new ArrayList<>();
		reviews.add(review1);
		reviews.add(review2);

		when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));
		when(reviewRepository.findReviewsByStoreId(store.getId())).thenReturn(
			Collections.singletonList(review2));

		List<Response> responseList = reviewService.getStoreReviews(store.getId());

		assertNotNull(responseList);
		assertEquals(responseList.size(), 1);

		Response response = responseList.get(0);
		assertEquals(review2.getCustomer().getId(), response.getCustomerId());
		assertEquals(review2.getStore().getId(), response.getStoreId());
		assertEquals(review2.getRating(), response.getRating());
		assertEquals(review2.getComment(), response.getComment());
	}
}

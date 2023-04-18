package com.hotsix.omc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.entity.Review;
import com.hotsix.omc.domain.entity.Store;
import com.hotsix.omc.domain.form.customer.CustomerReviewForm;
import com.hotsix.omc.repository.CustomerRepository;
import com.hotsix.omc.repository.ReviewRepository;
import com.hotsix.omc.repository.StoreRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CustomerReviewTest {

	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private CustomerRepository customerRepository;
	@Mock
	private StoreRepository storeRepository;

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
	@DisplayName ("리뷰 작성 성공")
	void success_addReview() {
		// given
		Customer customer = new Customer();
		customer.setId(1L);
		Store store = new Store();
		store.setId(1L);
		CustomerReviewForm.Request request = CustomerReviewForm.Request.builder()
			.customerId(1L)
			.storeId(1L)
			.comment("만족합니다.")
			.rating(4.5)
			.build();

		Review review = Review.builder()
			.id(1L)
			.customer(customer)
			.store(store)
			.comment("만족합니다.")
			.rating(4.5)
			.build();

		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
		when(reviewRepository.save(any(Review.class))).thenReturn(review);

		// when
		CustomerReviewForm.Response response = reviewService.addCustomerReview(request);

		// then
		assertNotNull(response);
		assertEquals(review.getCustomer().getId(), response.getCustomerId());
		assertEquals(review.getStore().getId(), response.getStoreId());
		assertEquals(review.getComment(), response.getComment());
	}

	@Test
	@DisplayName ("리뷰 작성 실패 - storeId 다른 경우")
	void fail_addReviewInvalidStoreId() {
		// given
		Customer customer = new Customer();
		customer.setId(1L);
		Store store = new Store();
		store.setId(1L);
		CustomerReviewForm.Request request = CustomerReviewForm.Request.builder()
			.customerId(1L)
			.storeId(2L)
			.comment("만족합니다.")
			.rating(4.5)
			.build();

		Review review = Review.builder()
			.id(1L)
			.customer(customer)
			.store(store)
			.comment("만족합니다.")
			.rating(4.5)
			.build();

		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
		when(reviewRepository.save(any(Review.class))).thenReturn(review);

		// when
		CustomerReviewForm.Response response = reviewService.addCustomerReview(request);

		// then
		assertNotNull(response);
		assertEquals(review.getCustomer().getId(), response.getCustomerId());
		assertNotEquals(review.getStore().getId(), response.getStoreId());
		assertEquals(review.getComment(), response.getComment());
	}

	@Test
	@DisplayName ("리뷰 작성 실패 - rating 다른 경우")
	void fail_addReviewInvalidRating() {
		// given
		Customer customer = new Customer();
		customer.setId(1L);
		Store store = new Store();
		store.setId(1L);
		CustomerReviewForm.Request request = CustomerReviewForm.Request.builder()
			.customerId(1L)
			.storeId(1L)
			.comment("만족합니다.")
			.rating(4.5)
			.build();

		Review review = Review.builder()
			.id(1L)
			.customer(customer)
			.store(store)
			.comment("만족합니다.")
			.rating(5.0)
			.build();

		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
		when(reviewRepository.save(any(Review.class))).thenReturn(review);

		// when
		CustomerReviewForm.Response response = reviewService.addCustomerReview(request);

		// then
		assertNotNull(response);
		assertEquals(review.getCustomer().getId(), response.getCustomerId());
		assertEquals(review.getStore().getId(), response.getStoreId());
		assertEquals(review.getComment(), response.getComment());
		assertNotEquals(review.getRating(), response.getRating());
	}

	@Test
	@DisplayName ("리뷰 수정 성공")
	void success_updateReview() {
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

		CustomerReviewForm.Request request = CustomerReviewForm.Request.builder()
			.customerId(1L)
			.storeId(1L)
			.comment("불만족합니다.")
			.rating(0.5)
			.build();

		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
		when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
		when(reviewRepository.save(any(Review.class))).thenReturn(review);

		// when
		CustomerReviewForm.Response response = reviewService.updateCustomerReview(request,
			review.getId());

		// then
		assertNotNull(response);
		assertEquals(review.getCustomer().getId(), response.getCustomerId());
		assertEquals(review.getStore().getId(), response.getStoreId());
		assertEquals(review.getComment(), response.getComment());
		assertEquals(review.getRating(), response.getRating());
	}

	@Test
	@DisplayName ("리뷰 삭제 성공")
	void success_deleteReview() {
		// given
		Review review = new Review();
		review.setId(1L);
		CustomerReviewForm.Request request = CustomerReviewForm.Request.builder()
			.customerId(1L)
			.storeId(1L)
			.comment("만족합니다.")
			.rating(4.5)
			.build();

		// when
		when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
		reviewService.deleteReview(review.getId());

		// then
		assertNotEquals(review.getComment(), request.getComment());
		assertNotEquals(review.getRating(), request.getRating());
	}
}
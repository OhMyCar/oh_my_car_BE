package com.hotsix.omc.service;

import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.entity.Review;
import com.hotsix.omc.domain.entity.Store;
import com.hotsix.omc.domain.form.customer.CustomerReviewForm;
import com.hotsix.omc.domain.form.customer.CustomerReviewForm.Response;
import com.hotsix.omc.domain.form.store.StoreReviewForm;
import com.hotsix.omc.domain.form.store.StoreReviewForm.Request;
import com.hotsix.omc.exception.ErrorCode;
import com.hotsix.omc.exception.UsersException;
import com.hotsix.omc.repository.CustomerRepository;
import com.hotsix.omc.repository.ReviewRepository;
import com.hotsix.omc.repository.StoreRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final CustomerRepository customerRepository;
	private final StoreRepository storeRepository;

	public Response addCustomerReview(CustomerReviewForm.Request request) {

		Customer customer = customerRepository.findById(request.getCustomerId())
			.orElseThrow(() -> new UsersException(ErrorCode.EMAIL_NOT_EXIST));

		Store store = storeRepository.findById(request.getStoreId())
			.orElseThrow(() -> new UsersException(ErrorCode.STORE_NOT_FOUND));

		// 이미 작성된 리뷰가 있을 시 작성 불가
		if (reviewRepository.existsByCustomerIdAndStoreId(request.getCustomerId(), request.getStoreId())) {
			throw new UsersException(ErrorCode.ALREADY_REGISTERED_REVIEW);
		}

		reviewRepository.save(Review.builder()
			.customer(customer)
			.store(store)
			.name(customer.getName())
			.comment(request.getComment())
			.rating(request.getRating())
			.build());
		return Response.from(request);
		// TODO : 예약고객과 업체 매핑(시간이 너무 잡아먹어서 다른 기능들 먼저 구현 후 추후에 리팩토링예정)
	}

	public Response updateCustomerReview(CustomerReviewForm.Request request, Long reviewId) {
		Customer customer = customerRepository.findById(request.getCustomerId())
			.orElseThrow(() -> new UsersException(ErrorCode.EMAIL_NOT_EXIST));
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new UsersException(ErrorCode.REVIEW_NOT_EXIST));

		// 본인 리뷰가 아닌 경우 수정 불가
		if (review.getCustomer().getId() != request.getCustomerId()) {
			throw new UsersException(ErrorCode.REVIEW_NOT_MATCH);
		}

		review.setComment(request.getComment());
		review.setRating(request.getRating());

		reviewRepository.save(review);

		return Response.from(request);
	}

	public void deleteReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new UsersException(ErrorCode.REVIEW_NOT_EXIST));

		reviewRepository.delete(review);
	}

	public List<CustomerReviewForm.Response> getStoreReviews(Long storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new UsersException(ErrorCode.REVIEW_NOT_EXIST));

		List<Review> reviews = reviewRepository.findReviewsByStoreId(storeId);
		List<Response> responseList = new ArrayList<>();
		for (Review review : reviews) {
			Response reviewResponse = Response.builder()
				.customerId(review.getCustomer().getId())
				.storeId(review.getStore().getId())
				.comment(review.getComment())
				.rating(review.getRating())
				.build();
			responseList.add(reviewResponse);
		}
		return responseList;
	}

	public StoreReviewForm.Response replyReview(Long reviewId, Request request) {

		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new UsersException(ErrorCode.REVIEW_NOT_EXIST));

		if (review.getCustomer().getId() != request.getCustomerId()) {
			throw new UsersException(ErrorCode.REVIEW_NOT_MATCH);
		}

		if (review.getStore().getId() != request.getStoreId()) {
			throw new UsersException(ErrorCode.REVIEW_NOT_MATCH);
		}

		review.setReply(request.getReply());
		reviewRepository.save(review);

		return StoreReviewForm.Response.from(request);
	}
}

package com.hotsix.omc.service;

import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.entity.Review;
import com.hotsix.omc.domain.entity.Store;
import com.hotsix.omc.domain.form.customer.CustomerReviewForm;
import com.hotsix.omc.domain.form.customer.CustomerReviewForm.Response;
import com.hotsix.omc.exception.ErrorCode;
import com.hotsix.omc.exception.UsersException;
import com.hotsix.omc.repository.CustomerRepository;
import com.hotsix.omc.repository.ReviewRepository;
import com.hotsix.omc.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final CustomerRepository customerRepository;
	private final StoreRepository storeRepository;

	public Response review(CustomerReviewForm.Request request) {
		Customer customer = customerRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new UsersException(ErrorCode.EMAIL_NOT_EXIST));

		Store store = storeRepository.findByAddress_Zipcode(request.getZipcode())
			.orElseThrow(() -> new UsersException(ErrorCode.STORE_NOT_FOUND));

		reviewRepository.save(Review.builder()
			.customer(customer)
			.store(store)
			.name(customer.getName())
			.comment(request.getComment())
			.rating(request.getRating())
			.build());
		return Response.from(request);
	}
}

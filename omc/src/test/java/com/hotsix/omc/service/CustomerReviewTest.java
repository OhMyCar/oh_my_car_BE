package com.hotsix.omc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hotsix.omc.domain.form.customer.CustomerReviewForm.Request;
import com.hotsix.omc.domain.form.customer.CustomerReviewForm.Response;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomerReviewTest {

	@Autowired
	private ReviewService reviewService;

	@Test
	void review() {
		Request request = Request.builder()
			.email("ceh201312@naver.com")
			.zipcode("00000")
			.comment("너무너무너무너무너무너무너무너무만족합니다.")
			.rating(4.0)
			.build();

		Response response = reviewService.review(request);
		Assert.notNull(response);
		assertEquals(response.getEmail(), "ceh201312@naver.com");
		assertEquals(response.getComment(), "너무너무너무너무너무너무너무너무만족합니다.");
		assertEquals(response.getRating(), 4.0);
	}
}

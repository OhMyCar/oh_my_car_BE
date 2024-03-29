package com.hotsix.omc.domain.form.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CustomerReviewForm {
	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Request {

		private long customerId;
		private long storeId;
		private String comment;
		private double rating;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Response {
		private long customerId;
		private long storeId;
		private String comment;
		private double rating;

		public static Response from(Request request) {
			return Response.builder()
				.customerId(request.customerId)
				.storeId(request.storeId)
				.comment(request.comment)
				.rating(request.rating)
				.build();
		}
	}
}
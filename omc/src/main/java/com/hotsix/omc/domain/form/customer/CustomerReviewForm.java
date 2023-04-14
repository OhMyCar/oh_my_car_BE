package com.hotsix.omc.domain.form.customer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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

		@NotBlank(message = "이메일은 필수 입력값입니다.")
		@Email
		private String email;
		private String zipcode;
		private String comment;
		private double rating;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Response {
		private String email;
		private String comment;
		private double rating;

		public static Response from(Request request) {
			return Response.builder()
				.email(request.email)
				.comment(request.comment)
				.rating(request.rating)
				.build();
		}
	}
}

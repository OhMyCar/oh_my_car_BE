package com.hotsix.omc.domain.form.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StoreReviewForm {
	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Request {
		private long customerId;
		private long storeId;
		private String reply;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Response {
		private long customerId;
		private long storeId;
		private String reply;

		public static StoreReviewForm.Response from(StoreReviewForm.Request request) {
			return Response.builder()
				.customerId(request.customerId)
				.storeId(request.storeId)
				.reply(request.reply)
				.build();
		}
	}
}

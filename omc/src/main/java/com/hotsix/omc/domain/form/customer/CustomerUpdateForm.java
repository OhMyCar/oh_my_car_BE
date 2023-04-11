package com.hotsix.omc.domain.form.customer;

import com.hotsix.omc.domain.entity.Customer;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CustomerUpdateForm {

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Request {
		@NotBlank(message = "비밀번호를 입력해 주세요.")
		private String password;
		@NotBlank(message = "휴대폰 번호를 입력해 주세요.")
		private String phone;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {
		@Email
		private String email;
		private String name;

		public static Response from(Customer customer) {
			return Response.builder()
				.email(customer.getEmail())
				.name(customer.getName())
				.build();
		}
	}
}


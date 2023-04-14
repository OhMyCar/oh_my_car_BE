package com.hotsix.omc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.form.customer.CustomerUpdateForm.Request;
import com.hotsix.omc.domain.form.customer.CustomerUpdateForm.Response;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomerUpdateDeleteTest {

	@Autowired
	private CustomerService customerService;

	@Test
	@DisplayName("고객 - 수정 성공")
	public void update() throws Exception {
		Request request = Request.builder()
			.password("aaaaaab!!!!")
			.phone("010-1234-5678")
			.build();

		Response response = customerService.update(request, 15L);


		Assert.notNull(response);
		assertEquals(request.getPassword(), "aaaaaab!!!!");
		assertEquals(request.getPhone(), "010-1234-5678");
	}

	@Test
	@DisplayName("고객 - 탈퇴 성공")
	public void delete() throws Exception {
		Customer c = customerService.delete(17L);
		assertNotSame(c.getEmail(), "ceh2013@naver.com");
	}
}

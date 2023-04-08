package com.hotsix.omc.domain.form.customer;

import com.hotsix.omc.domain.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDeleteForm {

	private Long id;

	@Builder
	public CustomerDeleteForm(Customer customer) {
		this.id = customer.getId();
	}
}

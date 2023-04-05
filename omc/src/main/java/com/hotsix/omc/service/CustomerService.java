package com.hotsix.omc.service;

import com.hotsix.omc.domain.form.customer.CustomerSignupForm;

public interface CustomerService {
    CustomerSignupForm.Response register(CustomerSignupForm.Request form);
}

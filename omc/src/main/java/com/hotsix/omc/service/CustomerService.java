package com.hotsix.omc.service;

import com.hotsix.omc.domain.form.customer.CustomerSignupForm;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm.Request;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm.Response;

public interface CustomerService {
    Response register(Request form);

    String emailAuth(String uuid);
}

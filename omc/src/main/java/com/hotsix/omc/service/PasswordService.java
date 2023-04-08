package com.hotsix.omc.service;

import com.hotsix.omc.domain.form.customer.CustomerSignupForm.Request;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm.Response;

public interface PasswordService {
    void changePassword(String email, String password);

    String emailAuth(String uuid);
}

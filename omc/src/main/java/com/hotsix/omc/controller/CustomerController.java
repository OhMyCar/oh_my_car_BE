package com.hotsix.omc.controller;

import com.hotsix.omc.domain.form.customer.CustomerSignupForm.Request;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm.Response;
import com.hotsix.omc.service.CustomerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerServiceImpl customerService;
    @PostMapping
    public ResponseEntity<Response> registerMember(
            @RequestBody @Valid Request request) {
        return ResponseEntity.ok(customerService.register(request));
    }
}

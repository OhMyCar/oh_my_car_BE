package com.hotsix.omc.controller;

import com.hotsix.omc.domain.form.customer.CustomerLoginForm;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm.Request;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm.Response;
import com.hotsix.omc.domain.form.token.TokenInfo;
import com.hotsix.omc.service.CustomerServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
@Slf4j
public class CustomerController {
    private final CustomerServiceImpl customerService;
    @PostMapping("/signup")
    public ResponseEntity<Response> registerMember(
            @RequestBody @Valid Request request) {
        return ResponseEntity.ok(customerService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(
            @RequestBody @Valid CustomerLoginForm form){
        TokenInfo customerToken = customerService.login(form.getEmail(), form.getPassword());
        return ResponseEntity.ok(customerToken);
    }

    @GetMapping("/email-auth")
    public ResponseEntity<String> emailAuth(HttpServletRequest request){
        String uuid = request.getParameter("id");
        log.info("uuid = " + uuid.toString());

        String emailAuthKey = customerService.emailAuth(uuid);
        return ResponseEntity.ok(emailAuthKey);
    }
}

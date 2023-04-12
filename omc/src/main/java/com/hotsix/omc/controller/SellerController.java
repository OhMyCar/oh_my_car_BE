package com.hotsix.omc.controller;

import com.hotsix.omc.domain.form.customer.CustomerLoginForm;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm;
import com.hotsix.omc.domain.form.seller.SellerLoginForm;
import com.hotsix.omc.domain.form.seller.SellerSignupForm;
import com.hotsix.omc.domain.form.token.TokenInfo;
import com.hotsix.omc.service.SellerService;
import com.hotsix.omc.service.SellerServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
@Slf4j
public class SellerController {
    private final SellerServiceImpl sellerService;
    @PostMapping("/signup")
    public ResponseEntity<SellerSignupForm.Response> registerMember(
            @RequestBody @Valid SellerSignupForm.Request request) {
        return ResponseEntity.ok(sellerService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(
            @RequestBody @Valid SellerLoginForm form){
        TokenInfo sellerToken = sellerService.login(form.getEmail(), form.getPassword());
        return ResponseEntity.ok(sellerToken);
    }

    @GetMapping("/email-auth")
    public ResponseEntity<String> emailAuth(HttpServletRequest request){
        String uuid = request.getParameter("id");
        log.info("uuid = " + uuid.toString());

        String emailAuthKey = sellerService.emailAuth(uuid);
        return ResponseEntity.ok(emailAuthKey);
    }
}

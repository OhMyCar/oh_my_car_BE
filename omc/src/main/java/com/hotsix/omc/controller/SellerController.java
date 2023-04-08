package com.hotsix.omc.controller;

import com.hotsix.omc.jwt.JwtTokenProvider;
import com.hotsix.omc.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.hotsix.omc.domain.form.seller.StoreRegisterForm.*;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService storeService;

    @PostMapping
    public ResponseEntity<Response> registerStore(@RequestBody @Valid Request request) {
        return ResponseEntity.ok(storeService.registerStore(request));
    }
}

package com.hotsix.omc.controller;


import com.hotsix.omc.domain.form.seller.StoreRegisterForm;
import com.hotsix.omc.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

private final SellerService sellerService;

    @PostMapping
    public ResponseEntity<StoreRegisterForm.Response> registerStore(@RequestBody @Valid StoreRegisterForm.Request request) {
        return ResponseEntity.ok(sellerService.registerStore(request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StoreRegisterForm.Response> updateStore(@RequestBody @Valid StoreRegisterForm.Request request, @PathVariable Long id) {
        return ResponseEntity.ok(sellerService.updateStore(request, id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable Long id) {
        sellerService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}

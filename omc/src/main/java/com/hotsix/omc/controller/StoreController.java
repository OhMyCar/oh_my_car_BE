package com.hotsix.omc.controller;


import com.hotsix.omc.domain.dto.StoreDto;
import com.hotsix.omc.domain.form.seller.StoreRegisterForm;
import com.hotsix.omc.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final SellerService sellerService;

    @PostMapping
    public ResponseEntity<StoreRegisterForm.Response> registerStore(@RequestBody @Valid StoreRegisterForm.Request request) {
        return ResponseEntity.ok(sellerService.registerStore(request));
    }

    @PutMapping("/update/{storeId}")
    public ResponseEntity<StoreRegisterForm.Response> updateStore(@RequestBody @Valid StoreRegisterForm.Request request, @PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(sellerService.updateStore(request, storeId));
    }

    @GetMapping("/getInfo/{sellerId}")
    public ResponseEntity<List<StoreDto>> getInfo(@PathVariable("sellerId") Long sellerId) {
        return ResponseEntity.ok(sellerService.getInfo(sellerId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable Long id) {
        sellerService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}

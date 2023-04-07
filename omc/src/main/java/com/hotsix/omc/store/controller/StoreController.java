package com.hotsix.omc.store.controller;

import com.hotsix.omc.store.model.StoreRegisterForm;
import com.hotsix.omc.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<String> registerStore(@RequestBody @Valid StoreRegisterForm form) {
        return ResponseEntity.ok(storeService.registerStore(form));
    }
}

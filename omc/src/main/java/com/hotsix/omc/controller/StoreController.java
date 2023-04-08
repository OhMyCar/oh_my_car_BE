package com.hotsix.omc.controller;


import com.hotsix.omc.domain.form.store.StoreRegisterForm;
import com.hotsix.omc.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<String> registerStore(@RequestBody @Valid StoreRegisterForm form) {
        return ResponseEntity.ok(storeService.registerStore(form));
    }
}

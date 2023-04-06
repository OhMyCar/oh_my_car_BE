package com.hotsix.omc.service;

import com.hotsix.omc.store.model.StoreRegisterForm;
import com.hotsix.omc.store.service.StoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StoreServiceImplTest {

    @Autowired
    private StoreService storeService;

    @Test
    void register() {
        StoreRegisterForm form = StoreRegisterForm.builder()
                .email("test@test.com")
                .open("10:00")
                .close("18:00")
                .name("test")
                .tel("010-1234-1234")
                .city("**시")
                .street("**대로")
                .zipcode("00000")
                .category1(true)
                .category2(false)
                .build();
        System.out.println(storeService.registerStore(form));

    }


}
package com.hotsix.omc.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static com.hotsix.omc.domain.form.seller.StoreRegisterForm.*;

@SpringBootTest
class SellerServiceImplTest {

    @Autowired
    private SellerService storeService;

    @Test
    void register() {
        Request request = Request.builder()
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

        Response response = storeService.registerStore(request);
        Assert.notNull(response.getName());
        Assert.notNull(response.getTel());
        Assert.notNull(response.getAddress());
        Assert.notNull(response.getOpen());
        Assert.notNull(response.getClose());
        Assert.notNull(response.getCategories());
        System.out.println(response.getCategories().toString());


    }


}
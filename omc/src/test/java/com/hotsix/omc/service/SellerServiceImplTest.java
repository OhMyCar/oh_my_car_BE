package com.hotsix.omc.service;

import com.hotsix.omc.domain.dto.StoreDto;
import com.hotsix.omc.domain.entity.Address;
import com.hotsix.omc.domain.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.hotsix.omc.domain.form.seller.StoreRegisterForm.Request;
import static com.hotsix.omc.domain.form.seller.StoreRegisterForm.Response;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class SellerServiceImplTest {

    @Autowired
    private SellerService sellerService;

    @Test
    void register() {
        Request request = Request.builder()
                .email("test@test.com")
                .open("10:00")
                .close("18:00")
                .name("test")
                .tel("010-1234-1234")
                .city("서울시")
                .street("강남대로")
                .zipcode("00000")
                .category1(true)
                .category2(false)
                .category3(true)
                .category4(false)
                .category5(true)
                .build();

        Response response = sellerService.registerStore(request);
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(Category.MAINTENANCE);
        categoryList.add(Category.TUNING);
        categoryList.add(Category.NEW_CAR_PACKAGE);


        Assert.notNull(response);
        assertEquals(response.getName(), "test");
        assertEquals(response.getTel(), "010-1234-1234");
        assertEquals(response.getAddress(), "서울시 강남대로 00000");
        assertEquals(response.getOpen(), "10:00");
        assertEquals(response.getClose(), "18:00");
        assertIterableEquals(response.getCategories(), categoryList);


    }

    @Test
    void update() {
        Request request = Request.builder()
                .email("test@test.com")
                .open("10:00")
                .close("15:00")
                .name("test2")
                .tel("010-5678-5678")
                .city("서울시")
                .street("선릉로")
                .zipcode("00000")
                .category1(true)
                .category2(false)
                .category3(true)
                .category4(false)
                .category5(true)
                .build();

        Response response = sellerService.updateStore(request, 1L);
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(Category.MAINTENANCE);
        categoryList.add(Category.TUNING);
        categoryList.add(Category.NEW_CAR_PACKAGE);

        Assert.notNull(response);
        assertEquals(response.getName(), "test2");
        assertEquals(response.getTel(), "010-5678-5678");
        assertEquals(response.getAddress(), "서울시 선릉로 00000");
        assertEquals(response.getOpen(), "10:00");
        assertEquals(response.getClose(), "15:00");
        assertIterableEquals(response.getCategories(), categoryList);
    }




}
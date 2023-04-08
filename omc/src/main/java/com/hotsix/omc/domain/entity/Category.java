package com.hotsix.omc.domain.entity;


import com.hotsix.omc.domain.form.seller.StoreRegisterForm;

import java.util.ArrayList;
import java.util.List;

public enum Category {
    MAINTENANCE, PAINT, TUNING, WASH, NEW_CAR_PACKAGE;

    public static List<Category> of(StoreRegisterForm.Request request) {
        List<Category> category = new ArrayList<>();

        if (request.isCategory1()) {
            category.add(Category.MAINTENANCE);
        }
        if (request.isCategory2()) {
            category.add(Category.PAINT);
        }
        if (request.isCategory3()) {
            category.add(Category.TUNING);
        }
        if (request.isCategory4()) {
            category.add(Category.WASH);
        }
        if (request.isCategory5()) {
            category.add(Category.NEW_CAR_PACKAGE);
        }
        return category;
    }

}

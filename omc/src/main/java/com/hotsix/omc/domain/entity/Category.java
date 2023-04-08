package com.hotsix.omc.domain.entity;


import com.hotsix.omc.domain.form.store.StoreRegisterForm;

import java.util.ArrayList;
import java.util.List;

public enum Category {
    MAINTENANCE, PAINT, TUNING, WASH, NEW_CAR_PACKAGE;

    public static List<Category> of(StoreRegisterForm form) {
        List<Category> category = new ArrayList<>();

        if (form.isCategory1()) {
            category.add(Category.MAINTENANCE);
        }
        if (form.isCategory2()) {
            category.add(Category.PAINT);
        }
        if (form.isCategory3()) {
            category.add(Category.TUNING);
        }
        if (form.isCategory4()) {
            category.add(Category.WASH);
        }
        if (form.isCategory5()) {
            category.add(Category.NEW_CAR_PACKAGE);
        }
        return category;
    }

}

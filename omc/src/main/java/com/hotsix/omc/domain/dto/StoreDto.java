package com.hotsix.omc.domain.dto;

import com.hotsix.omc.domain.entity.Address;
import com.hotsix.omc.domain.entity.Category;
import com.hotsix.omc.domain.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StoreDto {

    private Long id;
    private String open;
    private String close;
    private double rating;
    private String name;
    private String tel;
    private Address address;
    private List<Category> categories;

    public static StoreDto from(Store store) {
        return new StoreDto(store.getId(), store.getOpen(), store.getClose(), store.getRating(), store.getName(),
                store.getTel(), store.getAddress(), store.getCategories());
    }
}

package com.hotsix.omc.domain.dto;

import com.hotsix.omc.domain.entity.Address;
import com.hotsix.omc.domain.entity.Category;
import com.hotsix.omc.domain.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StoreDto {
    private String open;
    private String close;
    private double rating;
    private String name;
    private String tel;
    private Address address;
    private List<Category> categories;

    public static StoreDto from(Store store) {
        return StoreDto.builder()
                .name(store.getName())
                .open(store.getOpen())
                .close(store.getClose())
                .tel(store.getTel())
                .address(store.getAddress())
                .categories(store.getCategories())
                .build();
    }

}

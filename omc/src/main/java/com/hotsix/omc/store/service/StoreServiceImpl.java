package com.hotsix.omc.store.service;


import com.hotsix.omc.domain.entity.Address;
import com.hotsix.omc.domain.entity.Category;
import com.hotsix.omc.domain.entity.Seller;
import com.hotsix.omc.domain.entity.Store;
import com.hotsix.omc.exception.CustomException;
import com.hotsix.omc.exception.ErrorCode;
import com.hotsix.omc.repository.SellerRepository;
import com.hotsix.omc.store.model.StoreRegisterForm;
import com.hotsix.omc.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final SellerRepository sellerRepository;
    private final StoreRepository storeRepository;

    @Override
    public String registerStore(StoreRegisterForm form) {
        Seller seller = sellerRepository.findByEmail(form.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.SELLER_NOT_FOUND));
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        List<Category> category = Category.of(form);

        storeRepository.save(Store.builder()
                .seller(seller)
                .open(form.getOpen())
                .close(form.getClose())
                .name(form.getName())
                .tel(form.getTel())
                .address(address)
                .categories(category)
                .build());
        return "업체 등록에 성공하였습니다.";
    }
}

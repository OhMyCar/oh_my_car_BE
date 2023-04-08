package com.hotsix.omc.service;


import com.hotsix.omc.domain.entity.Address;
import com.hotsix.omc.domain.entity.Category;
import com.hotsix.omc.domain.entity.Seller;
import com.hotsix.omc.domain.entity.Store;
import com.hotsix.omc.domain.form.seller.StoreRegisterForm;
import com.hotsix.omc.domain.form.seller.StoreRegisterForm.Response;
import com.hotsix.omc.exception.ErrorCode;
import com.hotsix.omc.exception.UsersException;
import com.hotsix.omc.repository.SellerRepository;
import com.hotsix.omc.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final StoreRepository storeRepository;

    @Override
    public Response registerStore(StoreRegisterForm.Request request) {
        Seller seller = sellerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsersException(ErrorCode.SELLER_NOT_FOUND));


        Address address = new Address(request.getCity(), request.getStreet(), request.getZipcode());
        List<Category> category = Category.of(request);

        storeRepository.save(Store.builder()
                .seller(seller)
                .open(request.getOpen())
                .close(request.getClose())
                .name(request.getName())
                .tel(request.getTel())
                .address(address)
                .categories(category)
                .build());
        return Response.from(request);
    }

    @Override
    public Response updateStore(StoreRegisterForm.Request request) {
        return null;
    }
}

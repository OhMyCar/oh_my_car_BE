package com.hotsix.omc.service;


import com.hotsix.omc.domain.dto.StoreDto;
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

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
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
    public Response updateStore(StoreRegisterForm.Request request, Long storeId) {
        Seller seller = sellerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsersException(ErrorCode.SELLER_NOT_FOUND));
        Store store = storeRepository.findByIdAndSellerId(storeId, seller.getId())
                .orElseThrow(() -> new UsersException(ErrorCode.STORE_NOT_FOUND));
        Address address = new Address(request.getCity(), request.getStreet(), request.getZipcode());
        List<Category> category = Category.of(request);

        store.setOpen(request.getOpen());
        store.setClose(request.getClose());
        store.setName(request.getName());
        store.setTel(request.getTel());
        store.setAddress(address);
        store.setCategories(category);

        storeRepository.save(store);

        return Response.from(request);
    }

    @Override
    public List<StoreDto> getInfo(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new UsersException(ErrorCode.SELLER_NOT_FOUND));
        List<Store> stores = storeRepository.findBySellerId(sellerId);

        List<StoreDto> infoList = new ArrayList<>();
        for (int i = 0; i < stores.size(); i++) {
            infoList.add(StoreDto.builder()
                    .name(stores.get(i).getName())
                    .open(stores.get(i).getOpen())
                    .close(stores.get(i).getClose())
                    .tel(stores.get(i).getTel())
                    .address(stores.get(i).getAddress())
                    .categories(stores.get(i).getCategories())
                    .build());
        }

        return infoList;
    }

    @Override
    public void deleteStore(Long id) {
        Store existingStore = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with id " + id));

        storeRepository.delete(existingStore);
    }
}

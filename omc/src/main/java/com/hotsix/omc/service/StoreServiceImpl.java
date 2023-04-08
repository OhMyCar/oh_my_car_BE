package com.hotsix.omc.service;

import com.hotsix.omc.domain.entity.Address;
import com.hotsix.omc.domain.entity.Category;
import com.hotsix.omc.domain.entity.Seller;
import com.hotsix.omc.domain.entity.Store;
import com.hotsix.omc.domain.form.store.StoreRegisterForm;
import com.hotsix.omc.exception.UsersException;
import com.hotsix.omc.repository.SellerRepository;
import com.hotsix.omc.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.hotsix.omc.exception.ErrorCode.SELLER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final SellerRepository sellerRepository;

    @Override
    public void deleteStore(Long id) {
        Store existingStore = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with id " + id));

        storeRepository.delete(existingStore);
    }

    @Override
    public String registerStore(StoreRegisterForm form) {
        Seller seller = sellerRepository.findByEmail(form.getEmail())
                .orElseThrow(() -> new UsersException(SELLER_NOT_FOUND));
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

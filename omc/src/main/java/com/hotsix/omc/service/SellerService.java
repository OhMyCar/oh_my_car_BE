package com.hotsix.omc.service;

import com.hotsix.omc.domain.dto.StoreDto;
import com.hotsix.omc.domain.form.seller.StoreRegisterForm;
import com.hotsix.omc.domain.form.seller.StoreRegisterForm.Response;

import java.util.List;

public interface SellerService {
    public Response registerStore(StoreRegisterForm.Request request);
    public Response updateStore(StoreRegisterForm.Request request, Long id);
    public List<StoreDto> getInfo(Long id);
    void deleteStore(Long id);
}

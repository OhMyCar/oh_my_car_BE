package com.hotsix.omc.service;

import com.hotsix.omc.domain.form.seller.StoreRegisterForm;
import com.hotsix.omc.domain.form.seller.StoreRegisterForm.Response;

public interface SellerService {
    public Response registerStore(StoreRegisterForm.Request request);
    public Response updateStore(StoreRegisterForm.Request request);
    void deleteStore(Long id);
}

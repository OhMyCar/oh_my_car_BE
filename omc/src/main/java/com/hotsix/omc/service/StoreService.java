package com.hotsix.omc.service;


import com.hotsix.omc.domain.form.store.StoreRegisterForm;

public interface StoreService {
    void deleteStore(Long id);
    public String registerStore(StoreRegisterForm form);
}

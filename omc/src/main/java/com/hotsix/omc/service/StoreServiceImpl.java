package com.hotsix.omc.service;

import com.hotsix.omc.domain.entity.Store;
import com.hotsix.omc.repository.StoreRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class StoreServiceImpl implements StoreService {

    private StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public void deleteStore(Long id) {
        Store existingStore = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with id " + id));

        storeRepository.delete(existingStore);
    }
}

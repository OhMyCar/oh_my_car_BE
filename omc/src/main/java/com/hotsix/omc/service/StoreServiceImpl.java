package com.hotsix.omc.service;

import com.hotsix.omc.domain.entity.Store;
import com.hotsix.omc.repository.StoreRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Override
    public void deleteStore(Long id) {
        Store existingStore = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with id " + id));

        storeRepository.delete(existingStore);
    }
}

package com.hotsix.omc.service;

import com.hotsix.omc.domain.dto.StoreDto;
import com.hotsix.omc.domain.entity.Store;
import com.hotsix.omc.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreSearchService {

    private final StoreRepository storeRepository;

    public List<StoreDto> findNearestStores(double currentLng, double currentLat) {
        List<Store> stores = storeRepository.findNearestStores(currentLng, currentLat);

        return stores.stream().map(StoreDto::from).collect(Collectors.toList());
    }

}

package com.hotsix.omc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.hotsix.omc.domain.dto.StoreDto;
import com.hotsix.omc.domain.entity.Address;
import com.hotsix.omc.domain.entity.Store;
import com.hotsix.omc.repository.StoreRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class StoreSearchServiceTest {

    @Mock
    private StoreRepository storeRepository;

    private StoreSearchService storeSearchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        storeSearchService = new StoreSearchService(storeRepository);
    }

    @Test
    void findNearestStores() {
        double currentLat = 37.500411;
        double currentLng = 127.031143;
        Store store1 = Store.builder()
                .id(1L)
                .name("Store1")
                .address(new Address("서울시", "강남구 테헤란로 114", "00000"))
                .latitude(37.501000)
                .longitude(127.030000)
                .build();
        Store store2 = Store.builder()
                .id(2L)
                .name("Store2")
                .address(new Address("서울시", "용산구 녹사평대로 150", "00000"))
                .latitude(37.502000)
                .longitude(127.031000)
                .build();
        List<Store> stores = Arrays.asList(store1, store2);
        when(storeRepository.findNearestStores(currentLat, currentLng)).thenReturn(stores);

        List<StoreDto> nearestStores = storeSearchService.findNearestStores(currentLat, currentLng);

        assertEquals(2, nearestStores.size());
        assertEquals(store1.getName(), nearestStores.get(0).getName());
        assertEquals(store2.getName(), nearestStores.get(1).getName());
    }
}

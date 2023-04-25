package com.hotsix.omc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KakaoMapsServiceTest {

    @InjectMocks
    private KakaoMapsService kakaoMapsService;

    private String fullAddress;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        fullAddress = "서울시 강남구 테헤란로 114";
    }

    @Test
    public void testGetLatLnt() {

        String geoJsonString = kakaoMapsService.getGeoJson(fullAddress);
        Map<String, Object> geocodeData = kakaoMapsService.parseGeocode(geoJsonString);

        assertThat(geocodeData).containsKeys("longitude", "latitude");
        assertEquals(geocodeData.get("longitude").toString().substring(0, 3), ("127"));
        assertEquals(geocodeData.get("latitude").toString().substring(0, 2), ("37"));
    }


}
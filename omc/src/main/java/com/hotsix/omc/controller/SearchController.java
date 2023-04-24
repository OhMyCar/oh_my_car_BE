package com.hotsix.omc.controller;

import com.hotsix.omc.domain.dto.StoreDto;
import com.hotsix.omc.service.StoreSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final StoreSearchService storeSearchService;

    @GetMapping("/nearest")
    public ResponseEntity<List<StoreDto>> getNearestStore(@RequestParam("lng") double currentLng, @RequestParam("lat") double currentLat) {
        return ResponseEntity.ok(storeSearchService.findNearestStores(currentLng, currentLat));
    }

}
package com.hotsix.omc.controller;

import com.hotsix.omc.Scraper.CarNewsScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/news")
public class CarNewsController {

    @Autowired
    private CarNewsScraper CarNewsScraper;

    @GetMapping("/car")
    public ResponseEntity<List<Map<String, String>>> getCarNews() throws IOException {
        List<Map<String, String>> news = CarNewsScraper.getCarNews();
        return ResponseEntity.ok(news);
    }
}
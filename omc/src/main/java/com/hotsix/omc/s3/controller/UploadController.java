package com.hotsix.omc.s3.controller;

import com.hotsix.omc.s3.dto.FileDetailDto;
import com.hotsix.omc.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/upload", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UploadController {
    private final FileUploadService fileUploadService;

    @PostMapping
    public ResponseEntity<FileDetailDto> post(
            @RequestPart("file") MultipartFile multipartFile) {
        return ResponseEntity.ok(fileUploadService.save(multipartFile));
    }
}

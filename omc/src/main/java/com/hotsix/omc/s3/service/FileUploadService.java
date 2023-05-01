package com.hotsix.omc.s3.service;

import com.hotsix.omc.s3.dto.FileDetailDto;
import com.hotsix.omc.s3.storage.AmazonS3ResourceStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final AmazonS3ResourceStorage amazonS3ResourceStorage;

    public FileDetailDto save(MultipartFile multipartFile) {
        FileDetailDto fileDetail = FileDetailDto.multipartOf(multipartFile);
        amazonS3ResourceStorage.store(fileDetail.getPath(), multipartFile);
        return fileDetail;
    }
}

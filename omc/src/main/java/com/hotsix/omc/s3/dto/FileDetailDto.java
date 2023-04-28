package com.hotsix.omc.s3.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class FileDetailDto {
    private String id;
    private String name;
    private String format;
    private String path;
    private long bytes;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public static FileDetailDto multipartOf(MultipartFile multipartFile) {
        final String fileId = com.hotsix.omc.util.MultipartUtil.createFileId();
        final String format = com.hotsix.omc.util.MultipartUtil.getFormat(multipartFile.getContentType());
        return FileDetailDto.builder()
                .id(fileId)
                .name(multipartFile.getOriginalFilename())
                .format(format)
                .path(com.hotsix.omc.util.MultipartUtil.createPath(fileId, format))
                .bytes(multipartFile.getSize())
                .build();
    }
}
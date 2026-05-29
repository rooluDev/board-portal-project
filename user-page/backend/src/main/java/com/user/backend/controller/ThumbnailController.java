package com.user.backend.controller;

import com.user.backend.common.exception.custom.ThumbnailNotFoundException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.dto.ThumbnailDto;
import com.user.backend.service.StorageService;
import com.user.backend.service.ThumbnailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Thumbnail Controller
 */
@RestController
@RequestMapping("/api")
public class ThumbnailController {

    private final ThumbnailService thumbnailService;
    private final StorageService storageService;

    public ThumbnailController(@Qualifier("thumbnailJpa") ThumbnailService thumbnailService,
                               StorageService storageService) {
        this.thumbnailService = thumbnailService;
        this.storageService = storageService;
    }

    /**
     * 썸네일 이미지 바이트 GET (로컬/S3 프로파일 공통)
     *
     * @param thumbnailId PathVariable
     * @return Thumbnail Image Bytes
     */
    @GetMapping("/thumbnail/{thumbnailId}")
    public ResponseEntity<byte[]> getImage(@PathVariable(name = "thumbnailId") Long thumbnailId) {

        ThumbnailDto thumbnailDto = thumbnailService.getThumbnailById(thumbnailId)
                .orElseThrow(() -> new ThumbnailNotFoundException(ErrorCode.THUMBNAIL_NOT_FOUND));

        byte[] imageBytes = storageService.downloadThumbnail(thumbnailDto);

        MediaType mediaType = thumbnailDto.getExtension().equalsIgnoreCase("png")
                ? MediaType.IMAGE_PNG
                : MediaType.IMAGE_JPEG;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(imageBytes);
    }
}

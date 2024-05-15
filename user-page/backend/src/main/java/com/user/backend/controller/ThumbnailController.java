package com.user.backend.controller;

import com.user.backend.common.exception.custom.DownloadFailException;
import com.user.backend.common.exception.custom.ThumbnailNotFoundException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.common.utils.StringUtils;
import com.user.backend.dto.ThumbnailDto;
import com.user.backend.service.ThumbnailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

/**
 * Thumbnail Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ThumbnailController {

    @Value("#{storage['path']}")
    private String path;
    private final ThumbnailService thumbnailService;

    /**
     * 썸네일 이미지 Resource GET
     *
     * @param thumbnailId PathVariable
     * @return Thumbnail Image Resource
     */
    @GetMapping("/thumbnail/{thumbnailId}")
    public ResponseEntity<Resource> getImage(@PathVariable(name = "thumbnailId") Long thumbnailId) {

        // DB에서 썸네일 데이터 가져오기
        ThumbnailDto thumbnailDto = thumbnailService.getThumbnailById(thumbnailId)
                .orElseThrow(() -> new ThumbnailNotFoundException(ErrorCode.THUMBNAIL_NOT_FOUND));

        // Path 설정
        String uri = path + StringUtils.parseToPath(thumbnailDto);
        // 리소스 생성
        Resource resource = null;
        try {
            resource = new UrlResource("file://" + uri);
        } catch (MalformedURLException e) {
            throw new DownloadFailException(ErrorCode.DOWNLOAD_FAIL);
        }

        return ResponseEntity.ok(resource);
    }
}

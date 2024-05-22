package com.admin.backend.service;

import com.admin.backend.dto.ThumbnailDto;

/**
 * Thumbnail Service
 */
public interface ThumbnailService {

    /**
     * Thumbnail DB 등록
     *
     * @param thumbnailDto  DB에 저장할 Thumbnail Dto
     */
    void addThumbnail(ThumbnailDto thumbnailDto);

    /**
     * Thumbnail DB 삭제
     *
     * @param fileId ( pk )
     * @return 삭제한 썸네일 PK
     */
    int deleteThumbnailByFileId(Long fileId);
}

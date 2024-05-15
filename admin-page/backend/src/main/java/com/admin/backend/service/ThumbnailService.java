package com.admin.backend.service;

import com.admin.backend.dto.ThumbnailDto;

public interface ThumbnailService {

    void addThumbnail(ThumbnailDto thumbnailDto);

    int deleteThumbnailByFileId(Long fileId);
}

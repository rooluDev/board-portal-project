package com.admin.backend.service;

import com.admin.backend.dto.ThumbnailDto;
import com.admin.backend.mapper.ThumbnailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class ThumbnailServiceImpl implements ThumbnailService {

    private final ThumbnailMapper thumbnailMapper;

    @Override
    public void addThumbnail(ThumbnailDto thumbnailDto) {
        thumbnailMapper.insertThumbnail(thumbnailDto);
    }

    @Override
    public int deleteThumbnailByFileId(Long fileId) {
        return thumbnailMapper.deleteThumbnailByFileId(fileId);
    }
}

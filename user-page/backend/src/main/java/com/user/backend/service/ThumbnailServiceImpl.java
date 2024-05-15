package com.user.backend.service;

import com.user.backend.dto.ThumbnailDto;
import com.user.backend.mapper.ThumbnailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * ThumbnailService 구현체
 */
@Service
@Primary
@RequiredArgsConstructor
public class ThumbnailServiceImpl implements ThumbnailService{

    private final ThumbnailMapper thumbnailMapper;

    @Override
    public void addThumbnail(ThumbnailDto thumbnailDto) {
        thumbnailMapper.insertThumbnail(thumbnailDto);
    }

    @Override
    public int deleteThumbnailByFileId(Long fileId) {
        return thumbnailMapper.deleteThumbnailByFileId(fileId);
    }

    @Override
    public Optional<ThumbnailDto> getThumbnailById(Long thumbnailId) {
        return thumbnailMapper.selectThumbnailByFileId(thumbnailId);
    }
}

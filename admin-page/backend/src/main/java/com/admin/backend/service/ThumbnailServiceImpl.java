package com.admin.backend.service;

import com.admin.backend.dto.ThumbnailDto;
import com.admin.backend.mapper.ThumbnailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * ThumbnailService Impl
 */
@Service
@Primary
@RequiredArgsConstructor
public class ThumbnailServiceImpl implements ThumbnailService {

    private final ThumbnailMapper thumbnailMapper;

    /**
     * 썸네일을 DB에 등록
     *
     * @param thumbnailDto DB에 저장할 썸네일 데이터
     */
    @Override
    public void addThumbnail(ThumbnailDto thumbnailDto) {
        thumbnailMapper.insertThumbnail(thumbnailDto);
    }

    /**
     * 파일 pk로 썸네일 삭제
     *
     * @param fileId 연결된 파일의 pk
     * @return 삭제된 행 수
     */
    @Override
    public int deleteThumbnailByFileId(Long fileId) {
        return thumbnailMapper.deleteThumbnailByFileId(fileId);
    }
}

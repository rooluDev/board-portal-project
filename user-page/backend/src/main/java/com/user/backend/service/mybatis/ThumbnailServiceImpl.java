package com.user.backend.service.mybatis;

import com.user.backend.dto.ThumbnailDto;
import com.user.backend.mapper.ThumbnailMapper;
import com.user.backend.service.ThumbnailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * ThumbnailService 구현체
 */
@Service("thumbnailMybatis")
@RequiredArgsConstructor
public class ThumbnailServiceImpl implements ThumbnailService {

    private final ThumbnailMapper thumbnailMapper;

    /**
     * 썸네일 DB 추가
     *
     * @param thumbnailDto 추가할 썸네일 정보
     */
    @Override
    public void addThumbnail(ThumbnailDto thumbnailDto) {
        thumbnailMapper.insertThumbnail(thumbnailDto);
    }

    /**
     * fileId에 연결된 썸네일 DB 삭제
     *
     * @param fileId 삭제할 썸네일의 파일 ID (tb_file pk)
     * @return 삭제된 row count
     */
    @Override
    public int deleteThumbnailByFileId(Long fileId) {
        return thumbnailMapper.deleteThumbnailByFileId(fileId);
    }

    /**
     * 썸네일 ID로 썸네일 정보 가져오기
     *
     * @param thumbnailId 썸네일 ID (tb_thumbnail pk)
     * @return 해당 thumbnailId의 ThumbnailDto Optional
     */
    @Override
    public Optional<ThumbnailDto> getThumbnailById(Long thumbnailId) {
        return thumbnailMapper.selectThumbnailByFileId(thumbnailId);
    }
}

package com.user.backend.mapper;

import com.user.backend.dto.ThumbnailDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

/**
 * ThumbnailMapper
 */
@Mapper
public interface ThumbnailMapper {

    /**
     * 썸네일 DB 추가
     *
     * @param thumbnailDto 추가할 썸네일 Dto
     */
    void insertThumbnail(ThumbnailDto thumbnailDto);

    /**
     * 썸네일 DB 삭제
     *
     * @param fileId tb_file ( pk )
     * @return 삭제된 row count
     */
    int deleteThumbnailByFileId(Long fileId);

    /**
     * 썸네일 DB 가져오기
     *
     * @param thumbnailId tb_thumbnail ( pk )
     * @return Optional<ThumbnailDto>
     */
    Optional<ThumbnailDto> selectThumbnailByFileId(Long thumbnailId);
}

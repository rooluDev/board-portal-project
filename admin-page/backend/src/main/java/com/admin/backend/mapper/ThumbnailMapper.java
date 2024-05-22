package com.admin.backend.mapper;

import com.admin.backend.dto.ThumbnailDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * Thumbnail Mapper
 */
@Mapper
public interface ThumbnailMapper {

    /**
     * INSERT tb_thumbnail
     *
     * @param thumbnailDto (file_id, original_name, physical_name, file_path, extension, size)
     */
    void insertThumbnail(ThumbnailDto thumbnailDto);

    /**
     * DELETE tb_thumbnail By id
     *
     * @param fileId ( pk )
     * @return 삭제한 PK
     */
    int deleteThumbnailByFileId(Long fileId);
}

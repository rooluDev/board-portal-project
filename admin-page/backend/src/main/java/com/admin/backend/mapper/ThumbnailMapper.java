package com.admin.backend.mapper;

import com.admin.backend.dto.ThumbnailDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ThumbnailMapper {

    void insertThumbnail(ThumbnailDto thumbnailDto);

    int deleteThumbnailByFileId(Long fileId);
}

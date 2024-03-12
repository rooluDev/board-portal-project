package com.admin.backend.mapper;

import com.admin.backend.dto.FileDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * tb_file DB Mapper
 */
@Mapper
public interface FileMapper {

    /**
     * tb_file DB INSERT
     *
     * @param fileDto
     */
    void insertFile(FileDto fileDto);
}

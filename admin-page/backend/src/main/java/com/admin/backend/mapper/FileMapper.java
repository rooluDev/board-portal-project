package com.admin.backend.mapper;

import com.admin.backend.dto.FileDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

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

    /**
     * tb_file DB SELECT By BoardId And BoardType
     *
     * @param boardId
     * @param boardType
     * @return
     */
    List<FileDto> selectFileListByBoardId(@Param("boardId") Long boardId, @Param("boardType") String boardType);

    /**
     * SELECT tb_file By FileId
     *
     * @param fileId
     * @return
     */
    Optional<FileDto> selectFileById(Long fileId);
}

package com.user.backend.mapper;

import com.user.backend.dto.FileDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * tb_file Mapper
 */
@Mapper
public interface FileMapper {

    /**
     * INSERT tb_file
     *
     * @param fileDto ( board_type, board_id, original_name, physical_name, file_path, extension, size )
     */
    void insertFile(FileDto fileDto);

    /**
     * SELECT tb_file By BoardId And BoardType
     *
     * @param boardId   boardId
     * @param boardType boardType
     * @return boardType에 boardId의 등록된 파일 리스트
     */
    List<FileDto> selectFileListByBoardId(@Param("boardId") Long boardId, @Param("boardType") String boardType);

    /**
     * SELECT tb_file By FileId
     *
     * @param fileId ( pk )
     * @return fileId와 일치하는 File
     */
    Optional<FileDto> selectFileById(Long fileId);

    /**
     * DELETE tb_file By FileId
     *
     * @param fileId ( pk )
     */
    void deleteFileById(Long fileId);

    /**
     * SELECT File Row Count by BoardID
     *
     * @param boardId   boardId
     * @param boardType boardType
     * @return boardType에 boardId의 등록된 파일 리스트의 수
     */
    int selectRowCountByBoardId(@Param("boardId") Long boardId, @Param("boardType") String boardType);
}

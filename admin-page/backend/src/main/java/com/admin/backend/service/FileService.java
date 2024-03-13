package com.admin.backend.service;

import com.admin.backend.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * File Service Interface
 */
public interface FileService {

    /**
     * boardType에 boardId에 파일 추가
     *
     * @param fileList
     * @param boardType
     * @param boardId
     * @return
     */
    List<FileDto> addFile(MultipartFile[] fileList, String boardType, Long boardId);

    /**
     * boardType에 등록된 board 내 파일 가져오기
     *
     * @param boardId
     * @param boardType
     * @return
     */
    List<FileDto> getFileListByBoardId(Long boardId, String boardType);

    /**
     * fileId로 File 가져오기
     *
     * @param fileId
     * @return
     */
    Optional<FileDto> getFileById(Long fileId);

}

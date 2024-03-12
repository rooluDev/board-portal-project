package com.admin.backend.service;

import com.admin.backend.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
}

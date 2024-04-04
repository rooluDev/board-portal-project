package com.admin.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * FileStorageService Interface
 */
public interface FileStorageService {

    /**
     * File List 물리적으로 저장 후 DB에 저장
     *
     * @param fileList  저장할 파일리스트
     * @param boardId   bardId ( pk )
     * @param boardType boardType
     * @throws IOException IOException
     */
    void fileStorage(MultipartFile[] fileList, Long boardId, String boardType) throws IOException;
}

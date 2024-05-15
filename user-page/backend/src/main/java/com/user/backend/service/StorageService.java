package com.user.backend.service;

import com.user.backend.dto.FileDto;
import com.user.backend.dto.ThumbnailDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Storage Service
 */
public interface StorageService {

    /**
     * Multipart File 리스트 물리적 파일 생성
     *
     * @param multipartFiles 저장할 파일
     * @param boardType 보드 타입
     * @return 저장된 파일들 FileDto 리스트
     */
    List<FileDto> storageFileList(MultipartFile[] multipartFiles, String boardType);

    /**
     * FileDto로 썸네일 물리적 생성
     *
     * @param fileDto 생성할 원본 파일
     * @return 생성된 Thumbnail의 객체
     */
    ThumbnailDto storageThumbnailFromFile(FileDto fileDto);
}

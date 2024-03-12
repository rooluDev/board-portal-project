package com.admin.backend.service;

import com.admin.backend.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Upload Service Interface
 */
public interface UploadService {

    /**
     * file을 물리적 파일로 업로드 하는 메소드
     *
     * @param fileDtoList
     */
    void uploadFile(List<FileDto> fileDtoList, MultipartFile[] fileList) throws IOException;
}
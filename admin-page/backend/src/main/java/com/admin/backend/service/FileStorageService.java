package com.admin.backend.service;

import com.admin.backend.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
     * @param thumbnail 썸네일 저장 할지
     */
    void storageFileList(MultipartFile[] fileList, Long boardId, String boardType, boolean thumbnail);

    /**
     * 썸네일 DB저장 및 물리적 파일 저장
     *
     * @param fileDto 썸네일로 저장할 FileDto
     */
    void storageThumbnail(FileDto fileDto);

    /**
     * 파일 리스트 삭제
     *
     * @param deleteFileIdList 삭제할 파일들의 pk 리스트
     * @return 썸네일로 만든 파일 대상이 삭제가 되었는지
     */
    boolean deleteFileList(List<Long> deleteFileIdList);
}

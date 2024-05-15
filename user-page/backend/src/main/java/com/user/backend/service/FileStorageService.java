package com.user.backend.service;

import com.user.backend.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * File Storage Service
 */
public interface FileStorageService {

    /**
     * Multipart File List DB저장 및 물리적 파일 저장
     *
     * @param fileList 저장할 파일 리스트
     * @param boardId 게시판 번호
     * @param boardType 게시판 타입
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

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
     * file 등록
     *
     * @param fileList 추가 될 파일 리스트
     * @param boardType boardType
     * @param boardId boardId
     * @return 추가한 File 리스트
     */
    List<FileDto> addFile(MultipartFile[] fileList, String boardType, Long boardId);

    /**
     * 특정 게시물에 있는 file 리스트 가져오기
     *
     * @param boardId boardId
     * @param boardType boardType
     * @return boardType boardId에 있는 파일 리스트
     */
    List<FileDto> getFileListByBoardId(Long boardId, String boardType);

    /**
     * file 가져오기
     *
     * @param fileId ( pk )
     * @return fileId와 일치하는 File
     */
    Optional<FileDto> getFileById(Long fileId);

    /**
     * file 삭제
     *
     * @param deleteFileIdList pk list
     */
    void deleteFileList(List<Long> deleteFileIdList);

    /**
     * 게시물 내 존재하는 file의 수
     *
     * @param boardId boardId
     * @param boardType boardType
     * @return file의 수
     */
    int getRowCountByBoardId(Long boardId, String boardType);

}

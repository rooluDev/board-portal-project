package com.admin.backend.service;

import com.admin.backend.common.utils.MultipartFileUtils;
import com.admin.backend.dto.FileDto;
import com.admin.backend.mapper.FileMapper;
import com.admin.backend.mapper.ThumbnailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * File Service 구현체
 */
@Primary
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

    /**
     * 파일 리스트를 DB에 등록
     *
     * @param fileList DB에 저장할 파일 리스트
     * @param boardId  연결된 게시물의 pk
     * @return 첫 번째로 저장된 파일의 pk
     */
    @Override
    public Long addFileList(List<FileDto> fileList, Long boardId) {
        List<Long> pk = new ArrayList<>();
        for (FileDto fileDto : fileList) {
            fileDto.setBoardId(boardId);
            fileMapper.insertFile(fileDto);
            pk.add(fileDto.getFileId());
        }
        // return 첫번째 ID
        return pk.get(0);
    }

    /**
     * 특정 게시물에 있는 파일 리스트 가져오기
     *
     * @param boardId   게시물의 pk
     * @param boardType 게시판 타입
     * @return boardType에 boardId의 등록된 파일 리스트
     */
    @Override
    public List<FileDto> getFileListByBoardId(Long boardId, String boardType) {
        return fileMapper.selectFileListByBoardId(boardId, boardType);
    }

    /**
     * 파일 가져오기
     *
     * @param fileId 파일의 pk
     * @return fileId와 일치하는 파일
     */
    @Override
    public Optional<FileDto> getFileById(Long fileId) {
        return fileMapper.selectFileById(fileId);
    }

    /**
     * 파일 리스트 삭제
     *
     * @param deleteFileIdList 삭제할 파일들의 pk 리스트
     */
    @Override
    public void deleteFileList(List<Long> deleteFileIdList) {
        for(Long fileId : deleteFileIdList){
            fileMapper.deleteFileById(fileId);
        }
    }

    /**
     * 게시물 내 존재하는 파일의 수
     *
     * @param boardId   게시물의 pk
     * @param boardType 게시판 타입
     * @return 해당 게시물에 등록된 파일의 수
     */
    @Override
    public int getRowCountByBoardId(Long boardId, String boardType) {
        return fileMapper.selectRowCountByBoardId(boardId, boardType);
    }
}

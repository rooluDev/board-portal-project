package com.user.backend.service.mybatis;

import com.user.backend.dto.FileDto;
import com.user.backend.mapper.FileMapper;
import com.user.backend.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * File Service 구현체
 */
@Service("fileMybatis")
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

    /**
     * 파일 리스트를 DB에 등록하고 저장된 FileDto 리스트 반환
     *
     * @param fileList 저장할 파일 Dto 리스트
     * @param boardId  파일이 속한 게시물 ID
     * @return DB에 저장된 파일 Dto 리스트
     */
    @Override
    public List<FileDto> addFileList(List<FileDto> fileList, Long boardId) {
        for (FileDto fileDto : fileList) {
            fileDto.setBoardId(boardId);
            fileMapper.insertFile(fileDto);
        }
        return fileList;
    }

    /**
     * 특정 게시물에 있는 파일 리스트 가져오기
     *
     * @param boardId   게시물 ID
     * @param boardType 게시판 타입
     * @return 해당 게시물의 파일 리스트
     */
    @Override
    public List<FileDto> getFileListByBoardId(Long boardId, String boardType) {
        return fileMapper.selectFileListByBoardId(boardId, boardType);
    }

    /**
     * 파일 ID로 파일 정보 가져오기
     *
     * @param fileId 파일 ID (pk)
     * @return 해당 파일 ID의 파일 Optional
     */
    @Override
    public Optional<FileDto> getFileById(Long fileId) {
        return fileMapper.selectFileById(fileId);
    }

    /**
     * 파일 ID 리스트에 해당하는 파일들을 DB에서 삭제
     *
     * @param deleteFileIdList 삭제할 파일들의 ID 리스트
     */
    @Override
    public void deleteFileList(List<Long> deleteFileIdList) {
        for (Long fileId : deleteFileIdList) {
            fileMapper.deleteFileById(fileId);
        }
    }

    /**
     * 특정 게시물에 속한 파일 개수 조회
     *
     * @param boardId   게시물 ID
     * @param boardType 게시판 타입
     * @return 해당 게시물의 파일 개수
     */
    @Override
    public int getFileCountByBoardId(Long boardId, String boardType) {
        return fileMapper.selectRowCountByBoardId(boardId, boardType);
    }
}

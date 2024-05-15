package com.user.backend.service;

import com.user.backend.dto.FileDto;
import com.user.backend.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * File Service 구현체
 */
@Primary
@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

    @Override
    public List<FileDto> addFileList(List<FileDto> fileList, Long boardId) {
        for (FileDto fileDto : fileList) {
            fileDto.setBoardId(boardId);
            fileMapper.insertFile(fileDto);
        }
        return fileList;
    }

    @Override
    public List<FileDto> getFileListByBoardId(Long boardId, String boardType) {
        return fileMapper.selectFileListByBoardId(boardId, boardType);
    }

    @Override
    public Optional<FileDto> getFileById(Long fileId) {
        return fileMapper.selectFileById(fileId);
    }

    @Override
    public void deleteFileList(List<Long> deleteFileIdList) {
        for (Long fileId : deleteFileIdList) {
            fileMapper.deleteFileById(fileId);
        }
    }

    @Override
    public int getFileCountByBoardId(Long boardId, String boardType) {
        return fileMapper.selectRowCountByBoardId(boardId, boardType);
    }
}

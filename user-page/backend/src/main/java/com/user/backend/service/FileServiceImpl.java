package com.user.backend.service;

import com.user.backend.common.utils.MultipartFileUtils;
import com.user.backend.dto.FileDto;
import com.user.backend.mapper.FileMapper;
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

    @Override
    public List<FileDto> addFile(MultipartFile[] fileList, String boardType, Long boardId) {

        List<FileDto> addedFileList = new ArrayList<>();
        for (MultipartFile file : fileList) {
            if (!file.isEmpty()) {
                // 객체 생성
                FileDto fileDto = FileDto.builder()
                        .boardType(boardType)
                        .boardId(boardId)
                        .originalName(file.getOriginalFilename())
                        .physicalName(UUID.randomUUID().toString())
                        .filePath("/" + boardType)
                        .extension(MultipartFileUtils.extractExtension(file))
                        .size(file.getSize())
                        .build();

                // db 저장
                fileMapper.insertFile(fileDto);
                addedFileList.add(fileDto);
            }
        }

        return addedFileList;
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
        for(Long fileId : deleteFileIdList){
            fileMapper.deleteFileById(fileId);
        }
    }

    @Override
    public int getRowCountByBoardId(Long boardId, String boardType) {
        return fileMapper.selectRowCountByBoardId(boardId, boardType);
    }
}

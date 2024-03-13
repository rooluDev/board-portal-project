package com.admin.backend.service;

import com.admin.backend.common.utils.MultipartFileUtils;
import com.admin.backend.dto.FileDto;
import com.admin.backend.mapper.FileMapper;
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
public class FileServiceImpl implements FileService{

    public static final String PATH = "/Users/user/upload/";
    private final FileMapper fileMapper;

    @Override
    public List<FileDto> addFile(MultipartFile[] fileList, String boardType, Long boardId) {
        List<FileDto> addedFileList = new ArrayList<>();
        for(MultipartFile file : fileList){
            if(!file.isEmpty()){
                FileDto fileDto = FileDto.builder()
                        .boardType(boardType)
                        .boardId(boardId)
                        .originalName(file.getOriginalFilename())
                        .physicalName(UUID.randomUUID().toString())
                        .filePath(PATH)
                        .extension(MultipartFileUtils.extractExtension(file))
                        .size(file.getSize())
                        .build();
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
}

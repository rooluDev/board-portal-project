package com.admin.backend.service;

import com.admin.backend.dto.FileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * File Storage Service 구현체
 */
@Service
@Primary
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService{

    private final StorageService storageService;
    private final FileService fileService;

    @Override
    public void fileStorage(MultipartFile[] fileList, Long boardId, String boardType) throws IOException {
        List<FileDto> fileDtoList = storageService.storageFiles(fileList, boardType);
        fileService.addFile(fileDtoList,boardId);
    }
}

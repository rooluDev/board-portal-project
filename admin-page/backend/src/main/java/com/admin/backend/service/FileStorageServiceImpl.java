package com.admin.backend.service;

import com.admin.backend.common.exception.IllegalFileDataException;
import com.admin.backend.dto.FileDto;
import com.admin.backend.dto.ThumbnailDto;
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
public class FileStorageServiceImpl implements FileStorageService {

    private final StorageService storageService;
    private final FileService fileService;
    private final ThumbnailService thumbnailService;

    @Override
    public void storageFileList(MultipartFile[] fileList, Long boardId, String boardType, boolean thumbnail) {
        List<FileDto> fileDtoList = storageService.storageFileList(fileList, boardType);
        fileService.addFileList(fileDtoList, boardId);
        if (thumbnail && fileDtoList != null) {
            ThumbnailDto thumbnailDto = storageService.storageThumbnailFromFile(fileDtoList.get(0));
            thumbnailDto.setFileId(fileDtoList.get(0).getFileId());
            thumbnailService.addThumbnail(thumbnailDto);
        }
    }

    @Override
    public void storageThumbnail(FileDto fileDto) {
        ThumbnailDto thumbnailDto = storageService.storageThumbnailFromFile(fileDto);
        thumbnailDto.setFileId(fileDto.getFileId());
        thumbnailService.addThumbnail(thumbnailDto);
    }

    // TODO : void, boolean 따로?
    @Override
    public boolean deleteFileList(List<Long> deleteFileIdList) {
        boolean isThumbnailDeleted = false;
        for (Long fileId : deleteFileIdList) {
            int deletedRow = thumbnailService.deleteThumbnailByFileId(fileId);
            if (deletedRow == 1) {
                isThumbnailDeleted = true;
            }
        }
        fileService.deleteFileList(deleteFileIdList);
        return isThumbnailDeleted;
    }
}

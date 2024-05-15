package com.user.backend.service;


import com.user.backend.dto.FileDto;
import com.user.backend.dto.ThumbnailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Primary
public class FileStorageServiceImpl implements FileStorageService {

    private final StorageService storageService;
    private final FileService fileService;
    private final ThumbnailService thumbnailService;

    @Override
    public void storageFileList(MultipartFile[] fileList, Long boardId, String boardType, boolean thumbnail) {
        List<FileDto> fileDtoList = storageService.storageFileList(fileList, boardType);
        fileService.addFileList(fileDtoList, boardId);
        if (thumbnail) {
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

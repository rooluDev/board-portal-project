package com.user.backend.service;


import com.user.backend.dto.FileDto;
import com.user.backend.dto.ThumbnailDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * FileStorageService Impl
 */
@Service
@Primary
public class FileStorageServiceImpl implements FileStorageService {

    private final StorageService storageService;
    private final FileService fileService;
    private final ThumbnailService thumbnailService;

    public FileStorageServiceImpl(StorageService storageService,
                                  @Qualifier("fileJpa") FileService fileService,
                                  @Qualifier("thumbnailJpa") ThumbnailService thumbnailService) {
        this.storageService = storageService;
        this.fileService = fileService;
        this.thumbnailService = thumbnailService;
    }

    /**
     * Multipart 파일 리스트를 물리적으로 저장하고 DB에 등록, 필요시 썸네일도 생성
     *
     * @param fileList  저장할 멀티파트 파일 배열
     * @param boardId   파일이 속한 게시물 ID
     * @param boardType 게시판 타입
     * @param thumbnail 썸네일 생성 여부 (true이면 첫 번째 파일로 썸네일 생성)
     */
    @Override
    public void storageFileList(MultipartFile[] fileList, Long boardId, String boardType, boolean thumbnail) {
        List<FileDto> fileDtoList = storageService.storageFileList(fileList, boardType);
        fileDtoList = fileService.addFileList(fileDtoList, boardId);

        if (thumbnail) {
            ThumbnailDto thumbnailDto = storageService.storageThumbnailFromFile(fileDtoList.get(0));
            thumbnailDto.setFileId(fileDtoList.get(0).getFileId());
            thumbnailService.addThumbnail(thumbnailDto);
        }
    }

    /**
     * 기존 파일로부터 썸네일을 생성하여 물리적으로 저장하고 DB에 등록
     *
     * @param fileDto 썸네일 원본이 되는 파일 정보
     */
    @Override
    public void storageThumbnail(FileDto fileDto) {
        ThumbnailDto thumbnailDto = storageService.storageThumbnailFromFile(fileDto);
        thumbnailDto.setFileId(fileDto.getFileId());
        thumbnailService.addThumbnail(thumbnailDto);
    }

    /**
     * 파일 ID 리스트에 해당하는 파일들의 썸네일과 파일 정보를 DB에서 삭제
     *
     * @param deleteFileIdList 삭제할 파일들의 ID 리스트
     * @return 썸네일로 사용되던 파일이 삭제된 경우 true 반환
     */
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

package com.user.backend.service;

import com.user.backend.dto.FileDto;
import com.user.backend.dto.ThumbnailDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {

    @Mock
    private StorageService storageService;

    @Mock
    private FileService fileService;

    @Mock
    private ThumbnailService thumbnailService;

    private FileStorageServiceImpl fileStorageService;

    @BeforeEach
    void setUp() {
        fileStorageService = new FileStorageServiceImpl(storageService, fileService, thumbnailService);
    }

    @Test
    @DisplayName("파일 저장 (썸네일 포함) - 파일과 썸네일 모두 저장")
    void storageFileList_withThumbnail_savesFilesAndThumbnail() {
        // given
        MultipartFile[] files = {mock(MultipartFile.class)};
        Long boardId = 1L;
        String boardType = "gallery";

        FileDto savedFile = FileDto.builder()
                .fileId(1L)
                .boardType(boardType)
                .physicalName("uuid-1234")
                .filePath("/gallery")
                .extension("jpg")
                .build();
        ThumbnailDto thumbnail = ThumbnailDto.builder()
                .physicalName("thumb-uuid")
                .filePath("/thumbnail")
                .extension("jpg")
                .build();

        when(storageService.storageFileList(files, boardType)).thenReturn(List.of(savedFile));
        when(fileService.addFileList(List.of(savedFile), boardId)).thenReturn(List.of(savedFile));
        when(storageService.storageThumbnailFromFile(savedFile)).thenReturn(thumbnail);

        // when
        fileStorageService.storageFileList(files, boardId, boardType, true);

        // then
        verify(storageService).storageFileList(files, boardType);
        verify(fileService).addFileList(List.of(savedFile), boardId);
        verify(storageService).storageThumbnailFromFile(savedFile);
        verify(thumbnailService).addThumbnail(thumbnail);
    }

    @Test
    @DisplayName("파일 저장 (썸네일 없음) - 파일만 저장, 썸네일 생성 안함")
    void storageFileList_withoutThumbnail_savesFilesOnly() {
        // given
        MultipartFile[] files = {mock(MultipartFile.class)};
        Long boardId = 1L;
        String boardType = "free";

        FileDto savedFile = FileDto.builder()
                .fileId(1L)
                .boardType(boardType)
                .physicalName("uuid-5678")
                .filePath("/free")
                .extension("pdf")
                .build();

        when(storageService.storageFileList(files, boardType)).thenReturn(List.of(savedFile));
        when(fileService.addFileList(List.of(savedFile), boardId)).thenReturn(List.of(savedFile));

        // when
        fileStorageService.storageFileList(files, boardId, boardType, false);

        // then
        verify(storageService).storageFileList(files, boardType);
        verify(fileService).addFileList(List.of(savedFile), boardId);
        verify(storageService, never()).storageThumbnailFromFile(any());
        verify(thumbnailService, never()).addThumbnail(any());
    }

    @Test
    @DisplayName("파일 삭제 - 썸네일과 파일 모두 삭제")
    void deleteFileList_deletesFilesAndThumbnails() {
        // given
        List<Long> fileIds = List.of(1L, 2L, 3L);
        when(thumbnailService.deleteThumbnailByFileId(anyLong())).thenReturn(0);

        // when
        fileStorageService.deleteFileList(fileIds);

        // then
        verify(thumbnailService, times(3)).deleteThumbnailByFileId(anyLong());
        verify(fileService).deleteFileList(fileIds);
    }
}

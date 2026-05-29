package com.user.backend.controller;

import com.user.backend.common.exception.custom.FileNotFoundException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.dto.FileDto;
import com.user.backend.service.FileService;
import com.user.backend.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * File Controller
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class FileController {

    private final FileService fileService;
    private final StorageService storageService;

    public FileController(@Qualifier("fileJpa") FileService fileService,
                          StorageService storageService) {
        this.fileService = fileService;
        this.storageService = storageService;
    }

    /**
     * 파일 다운로드
     *
     * @param fileId PathVariable (pk)
     * @return 파일 바이트
     */
    @GetMapping("/file/{fileId}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable(name = "fileId") Long fileId) {
        FileDto fileDto = fileService.getFileById(fileId)
                .orElseThrow(() -> new FileNotFoundException(ErrorCode.FILE_NOT_FOUND));

        byte[] fileBytes = storageService.downloadFile(fileDto);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileDto.getOriginalName() + "\"")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileBytes.length))
                .body(fileBytes);
    }

    /**
     * 이미지 파일 인라인 표시
     *
     * @param fileId PathVariable (pk)
     * @return 이미지 바이트
     */
    @GetMapping("/file/{fileId}")
    public ResponseEntity<byte[]> getImage(@PathVariable(name = "fileId") Long fileId) {
        FileDto fileDto = fileService.getFileById(fileId)
                .orElseThrow(() -> new FileNotFoundException(ErrorCode.FILE_NOT_FOUND));

        byte[] imageBytes = storageService.downloadFile(fileDto);

        MediaType mediaType = "png".equalsIgnoreCase(fileDto.getExtension())
                ? MediaType.IMAGE_PNG
                : MediaType.IMAGE_JPEG;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(imageBytes);
    }
}

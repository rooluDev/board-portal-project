package com.user.backend.controller;

import com.user.backend.common.exception.custom.DownloadFailException;
import com.user.backend.common.exception.custom.FileNotFoundException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.common.utils.StringUtils;
import com.user.backend.dto.FileDto;
import com.user.backend.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * File Controller
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class FileController {

    @Value("#{storage['path']}")
    private String path;
    private final FileService fileService;

    public FileController(@Qualifier("fileJpa") FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 파일 다운로드 리소스 반환
     *
     * @param fileId ( pk )
     * @return 파일 리소스
     * @throws Exception
     */
    @GetMapping("/file/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable(name = "fileId") Long fileId) {
        // 파일 정보 가져오기
        FileDto file = fileService.getFileById(fileId).orElseThrow(() -> new FileNotFoundException(ErrorCode.FILE_NOT_FOUND));

        try {
            // 파일 정보 설정
            String filePathString = path + StringUtils.parseToPath(file);

            File filePath = Paths.get(filePathString).toFile();

            Resource resource = new InputStreamResource(Files.newInputStream(filePath.toPath()));

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getName() + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(filePath.length()))
                    .body(resource);

        } catch (IOException e) {
            throw new DownloadFailException(ErrorCode.DOWNLOAD_FAIL);
        }
    }


    /**
     * 이미지 파일 리소스 가져오기
     *
     * @param fileId ( pk )
     * @return 이미지 파일 리소스
     */
    @GetMapping("/file/{fileId}")
    public ResponseEntity<Resource> getImage(@PathVariable(name = "fileId") Long fileId) {

        // 파일 정보 가져오기
        FileDto fileDto = fileService.getFileById(fileId).orElseThrow(() -> new FileNotFoundException(ErrorCode.FILE_NOT_FOUND));

        // 파일 path
        String fullPath = path + StringUtils.parseToPath(fileDto);

        try {
            Resource resource = new UrlResource("file://" + fullPath);
            return ResponseEntity.ok(resource);
        } catch (MalformedURLException e) {
            throw new DownloadFailException(ErrorCode.DOWNLOAD_FAIL);
        }
    }
}

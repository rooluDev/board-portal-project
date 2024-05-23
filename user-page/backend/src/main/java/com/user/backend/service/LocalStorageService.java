package com.user.backend.service;

import com.user.backend.common.exception.custom.StorageFailException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.common.utils.MultipartFileUtils;
import com.user.backend.common.utils.StringUtils;
import com.user.backend.dto.FileDto;
import com.user.backend.dto.ThumbnailDto;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * LocalStorageService
 */
@Service
@Primary
public class LocalStorageService implements StorageService {

    @Value("#{storage['path']}")
    private String path;

    @Override
    public List<FileDto> storageFileList(MultipartFile[] multipartFiles, String boardType) throws StorageFailException {
        // 저장 된 파일 Dto 정보 저장될 리스트
        List<FileDto> savedFileList = new ArrayList<>();

        // 저장
        for (MultipartFile multipartFile : multipartFiles) {
            try {
                // 파일 Dto 생성
                FileDto fileDto = FileDto.builder()
                        .boardType(boardType)
                        .originalName(multipartFile.getOriginalFilename())
                        .physicalName(UUID.randomUUID().toString())
                        .filePath("/" + boardType)
                        .extension(MultipartFileUtils.extractExtension(multipartFile))
                        .size(multipartFile.getSize())
                        .build();

                // File 객체 생성
                String filePath = path + StringUtils.parseToPath(fileDto);
                File saveFile = new File(filePath);

                // 물리적 파일 생성
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), saveFile);

                // fileDto 저장
                savedFileList.add(fileDto);

            } catch (IOException e) {
                throw new StorageFailException(ErrorCode.STORAGE_FAIL);
            }
        }

        return savedFileList;
    }

    @Override
    public ThumbnailDto storageThumbnailFromFile(FileDto fileDto) {
        // 썸네일 Dto 생성
        ThumbnailDto thumbnailDto = ThumbnailDto.builder()
                .originalName(fileDto.getOriginalName())
                .physicalName(UUID.randomUUID().toString())
                .filePath("/thumbnail")
                .extension(fileDto.getExtension())
                .size(fileDto.getSize())
                .build();

        // 원본 파일 경로
        String sourceFilePath = path + StringUtils.parseToPath(fileDto);
        File file = new File(sourceFilePath);

        // 썸네일 저장 경로 생성
        String thumbnailPath = path + StringUtils.parseToPath(thumbnailDto);
        Path filePath = Paths.get(thumbnailPath);

        // 썸네일 생성
        createThumbNail(file, filePath);
        return thumbnailDto;

    }

    private void createThumbNail(File file, Path filePath) {
        try {
            Thumbnails.of(file)
                    .size(300, 300)
                    .toFile(filePath.toFile());
        } catch (IOException e) {
            throw new StorageFailException(ErrorCode.STORAGE_FAIL);
        }
    }
}

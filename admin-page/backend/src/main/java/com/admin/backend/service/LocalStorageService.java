package com.admin.backend.service;

import com.admin.backend.common.exception.StorageFailException;
import com.admin.backend.common.utils.MultipartFileUtils;
import com.admin.backend.common.utils.StringUtils;
import com.admin.backend.dto.FileDto;
import com.admin.backend.dto.ThumbnailDto;
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

@Service
@Primary
public class LocalStorageService implements StorageService {

    @Value("#{storage['path']}")
    private String path;

    @Override
    public List<FileDto> storageFileList(MultipartFile[] multipartFiles, String boardType) throws StorageFailException {
        List<FileDto> savedFileList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                try {
                    FileDto fileDto = FileDto.builder()
                            .boardType(boardType)
                            .originalName(multipartFile.getOriginalFilename())
                            .physicalName(UUID.randomUUID().toString())
                            .filePath("/" + boardType)
                            .extension(MultipartFileUtils.extractExtension(multipartFile))
                            .size(multipartFile.getSize())
                            .build();

                    String filePath = path + StringUtils.parseToPath(fileDto);
                    File saveFile = new File(filePath);

                    FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), saveFile);

                    savedFileList.add(fileDto);

                } catch (IOException e) {
                    throw new StorageFailException();
                }
            }
        }

        return savedFileList;
    }

    @Override
    public ThumbnailDto storageThumbnailFromFile(FileDto fileDto) {

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

        try {
            createThumbNail(file, filePath);
        } catch (IOException e) {
            throw new StorageFailException();
        }
        return thumbnailDto;
    }

    private void createThumbNail(File file, Path filePath) throws IOException {
        Thumbnails.of(file)
                .size(100, 100)
                .toFile(filePath.toFile());
    }
}

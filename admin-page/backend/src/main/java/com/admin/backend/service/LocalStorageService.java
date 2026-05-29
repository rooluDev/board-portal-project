package com.admin.backend.service;

import com.admin.backend.common.exception.StorageFailException;
import com.admin.backend.common.utils.MultipartFileUtils;
import com.admin.backend.common.utils.StringUtils;
import com.admin.backend.dto.FileDto;
import com.admin.backend.dto.ThumbnailDto;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
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
 * 로컬 파일 시스템을 이용한 StorageService 구현체 (dev 프로파일)
 */
@Service
@Profile("dev")
public class LocalStorageService implements StorageService {

    @Value("#{storage['path']}")
    private String path;

    /**
     * 파일 리스트를 로컬 파일 시스템에 물리적으로 저장
     *
     * @param multipartFiles 저장할 파일 배열
     * @param boardType      게시판 타입 (파일 저장 경로에 사용)
     * @return 저장된 파일들의 FileDto 리스트
     * @throws StorageFailException 파일 저장 실패 시 발생
     */
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

    /**
     * FileDto를 기반으로 썸네일을 로컬 파일 시스템에 물리적으로 생성
     *
     * @param fileDto 원본 파일 데이터
     * @return 생성된 썸네일의 ThumbnailDto
     * @throws StorageFailException 썸네일 생성 실패 시 발생
     */
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

    /**
     * 원본 파일을 100x100 크기의 썸네일로 생성
     *
     * @param file     원본 파일 객체
     * @param filePath 썸네일이 저장될 경로
     * @throws IOException 파일 처리 실패 시 발생
     */
    private void createThumbNail(File file, Path filePath) throws IOException {
        Thumbnails.of(file)
                .size(100, 100)
                .toFile(filePath.toFile());
    }
}

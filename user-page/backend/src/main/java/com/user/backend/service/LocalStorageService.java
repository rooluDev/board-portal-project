package com.user.backend.service;

import com.user.backend.common.exception.custom.DownloadFailException;
import com.user.backend.common.exception.custom.StorageFailException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.common.utils.MultipartFileUtils;
import com.user.backend.common.utils.StringUtils;
import com.user.backend.dto.FileDto;
import com.user.backend.dto.ThumbnailDto;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * LocalStorageService
 */
@Service
@Profile("dev")
public class LocalStorageService implements StorageService {

    @Value("#{storage['path']}")
    private String path;

    /**
     * Multipart 파일 리스트를 로컬 스토리지에 저장하고 FileDto 리스트 반환
     *
     * @param multipartFiles 저장할 멀티파트 파일 배열
     * @param boardType      게시판 타입 (파일 저장 경로 구분)
     * @return 저장된 파일들의 FileDto 리스트
     * @throws StorageFailException 파일 저장 실패 시
     */
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

    /**
     * FileDto에 해당하는 원본 파일을 읽어 로컬 스토리지에 썸네일을 생성하고 ThumbnailDto 반환
     *
     * @param fileDto 썸네일 원본이 되는 파일 정보
     * @return 생성된 썸네일의 ThumbnailDto
     */
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

    /**
     * 원본 파일로부터 썸네일 이미지 파일을 생성하여 지정된 경로에 저장
     *
     * @param file     원본 이미지 파일
     * @param filePath 썸네일을 저장할 경로
     * @throws StorageFailException 썸네일 생성 실패 시
     */
    private void createThumbNail(File file, Path filePath) {
        try {
            Thumbnails.of(file)
                    .size(300, 300)
                    .toFile(filePath.toFile());
        } catch (IOException e) {
            throw new StorageFailException(ErrorCode.STORAGE_FAIL);
        }
    }

    /**
     * 로컬 스토리지에서 썸네일 파일을 읽어 바이트 배열로 반환
     *
     * @param thumbnailDto 다운로드할 썸네일 정보
     * @return 썸네일 파일의 바이트 배열
     * @throws DownloadFailException 파일 읽기 실패 시
     */
    @Override
    public byte[] downloadThumbnail(ThumbnailDto thumbnailDto) {
        try {
            String filePath = path + StringUtils.parseToPath(thumbnailDto);
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new DownloadFailException(ErrorCode.DOWNLOAD_FAIL);
        }
    }

    /**
     * 로컬 스토리지에서 일반 파일을 읽어 바이트 배열로 반환
     *
     * @param fileDto 다운로드할 파일 정보
     * @return 파일의 바이트 배열
     * @throws DownloadFailException 파일 읽기 실패 시
     */
    @Override
    public byte[] downloadFile(FileDto fileDto) {
        try {
            String filePath = path + StringUtils.parseToPath(fileDto);
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new DownloadFailException(ErrorCode.DOWNLOAD_FAIL);
        }
    }
}

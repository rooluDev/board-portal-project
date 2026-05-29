package com.user.backend.service;

import com.user.backend.common.exception.custom.StorageFailException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.common.utils.MultipartFileUtils;
import com.user.backend.dto.FileDto;
import com.user.backend.dto.ThumbnailDto;
import jakarta.annotation.PostConstruct;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import com.user.backend.common.exception.custom.DownloadFailException;
import com.user.backend.common.exception.response.ErrorCode;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * S3 기반 파일 저장 서비스 (prod 프로파일 전용)
 */
@Service
@Profile("prod")
public class S3StorageService implements StorageService {

    @Value("#{storage['bucket']}")
    private String bucket;

    @Value("#{storage['region']}")
    private String region;

    private S3Client s3Client;

    /**
     * S3Client 초기화 (빈 생성 후 프로퍼티 주입이 완료된 뒤 실행)
     */
    @PostConstruct
    public void init() {
        s3Client = S3Client.builder()
                .region(Region.of(region))
                .build();
    }

    /**
     * Multipart 파일 리스트를 S3에 업로드하고 FileDto 리스트 반환
     *
     * @param multipartFiles 저장할 멀티파트 파일 배열
     * @param boardType      게시판 타입 (S3 키 경로 구분)
     * @return 업로드된 파일들의 FileDto 리스트
     * @throws StorageFailException 파일 업로드 실패 시
     */
    @Override
    public List<FileDto> storageFileList(MultipartFile[] multipartFiles, String boardType) {
        List<FileDto> savedFileList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            try {
                String physicalName = UUID.randomUUID().toString();
                String extension = MultipartFileUtils.extractExtension(multipartFile);
                String s3Key = boardType + "/" + physicalName + "." + extension;

                s3Client.putObject(
                        PutObjectRequest.builder()
                                .bucket(bucket)
                                .key(s3Key)
                                .contentType(multipartFile.getContentType())
                                .contentLength(multipartFile.getSize())
                                .build(),
                        RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize())
                );

                FileDto fileDto = FileDto.builder()
                        .boardType(boardType)
                        .originalName(multipartFile.getOriginalFilename())
                        .physicalName(physicalName)
                        .filePath("/" + boardType)
                        .extension(extension)
                        .size(multipartFile.getSize())
                        .build();

                savedFileList.add(fileDto);

            } catch (IOException e) {
                throw new StorageFailException(ErrorCode.STORAGE_FAIL);
            }
        }

        return savedFileList;
    }

    /**
     * S3에 저장된 원본 파일을 읽어 썸네일을 생성하고 S3에 업로드 후 ThumbnailDto 반환
     *
     * @param fileDto 썸네일 원본이 되는 파일 정보
     * @return 생성된 썸네일의 ThumbnailDto
     * @throws StorageFailException 썸네일 생성 또는 업로드 실패 시
     */
    @Override
    public ThumbnailDto storageThumbnailFromFile(FileDto fileDto) {
        String physicalName = UUID.randomUUID().toString();

        ThumbnailDto thumbnailDto = ThumbnailDto.builder()
                .originalName(fileDto.getOriginalName())
                .physicalName(physicalName)
                .filePath("/thumbnail")
                .extension(fileDto.getExtension())
                .size(fileDto.getSize())
                .build();

        // S3에서 원본 파일 다운로드
        String sourceKey = fileDto.getBoardType() + "/" + fileDto.getPhysicalName() + "." + fileDto.getExtension();
        byte[] originalBytes = s3Client.getObjectAsBytes(
                GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(sourceKey)
                        .build()
        ).asByteArray();

        // 메모리에서 썸네일 생성
        ByteArrayOutputStream thumbnailOutput = new ByteArrayOutputStream();
        try {
            Thumbnails.of(new ByteArrayInputStream(originalBytes))
                    .size(300, 300)
                    .outputFormat(fileDto.getExtension())
                    .toOutputStream(thumbnailOutput);
        } catch (IOException e) {
            throw new StorageFailException(ErrorCode.STORAGE_FAIL);
        }

        // 썸네일 S3 업로드
        byte[] thumbnailBytes = thumbnailOutput.toByteArray();
        String thumbnailKey = "thumbnail/" + physicalName + "." + fileDto.getExtension();
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(thumbnailKey)
                        .contentType("image/" + fileDto.getExtension())
                        .contentLength((long) thumbnailBytes.length)
                        .build(),
                RequestBody.fromBytes(thumbnailBytes)
        );

        return thumbnailDto;
    }

    /**
     * S3에서 썸네일 파일을 다운로드하여 바이트 배열로 반환
     *
     * @param thumbnailDto 다운로드할 썸네일 정보
     * @return 썸네일 파일의 바이트 배열
     * @throws DownloadFailException 다운로드 실패 시
     */
    @Override
    public byte[] downloadThumbnail(ThumbnailDto thumbnailDto) {
        String key = "thumbnail/" + thumbnailDto.getPhysicalName() + "." + thumbnailDto.getExtension();
        try {
            return s3Client.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .build()
            ).asByteArray();
        } catch (Exception e) {
            throw new DownloadFailException(ErrorCode.DOWNLOAD_FAIL);
        }
    }

    /**
     * S3에서 일반 파일을 다운로드하여 바이트 배열로 반환
     *
     * @param fileDto 다운로드할 파일 정보
     * @return 파일의 바이트 배열
     * @throws DownloadFailException 다운로드 실패 시
     */
    @Override
    public byte[] downloadFile(FileDto fileDto) {
        // filePath = "/gallery" → S3 key = "gallery/{uuid}.jpg"
        String prefix = fileDto.getFilePath().replaceFirst("^/", "");
        String key = prefix + "/" + fileDto.getPhysicalName() + "." + fileDto.getExtension();
        try {
            return s3Client.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .build()
            ).asByteArray();
        } catch (Exception e) {
            throw new DownloadFailException(ErrorCode.DOWNLOAD_FAIL);
        }
    }
}
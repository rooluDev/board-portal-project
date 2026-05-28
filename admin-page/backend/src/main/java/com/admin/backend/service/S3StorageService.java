package com.admin.backend.service;

import com.admin.backend.common.exception.StorageFailException;
import com.admin.backend.common.utils.MultipartFileUtils;
import com.admin.backend.dto.FileDto;
import com.admin.backend.dto.ThumbnailDto;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Profile("prod")
public class S3StorageService implements StorageService {

    @Value("#{storage['bucket']}")
    private String bucket;

    @Value("#{storage['region']}")
    private String region;

    private S3Client s3Client;

    @PostConstruct
    public void init() {
        s3Client = S3Client.builder()
                .region(Region.of(region))
                .build();
    }

    @Override
    public List<FileDto> storageFileList(MultipartFile[] multipartFiles, String boardType) {
        List<FileDto> savedFileList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
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
                    throw new StorageFailException();
                }
            }
        }

        return savedFileList;
    }

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

        String sourceKey = fileDto.getBoardType() + "/" + fileDto.getPhysicalName() + "." + fileDto.getExtension();
        byte[] originalBytes = s3Client.getObjectAsBytes(
                GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(sourceKey)
                        .build()
        ).asByteArray();

        ByteArrayOutputStream thumbnailOutput = new ByteArrayOutputStream();
        try {
            Thumbnails.of(new ByteArrayInputStream(originalBytes))
                    .size(100, 100)
                    .outputFormat(fileDto.getExtension())
                    .toOutputStream(thumbnailOutput);
        } catch (IOException e) {
            throw new StorageFailException();
        }

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
}
package com.admin.backend.service;

import com.admin.backend.common.utils.MultipartFileUtils;
import com.admin.backend.dto.FileDto;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Primary
public class LocalStorageService implements StorageService {

    @Value("#{file['file.path']}")
    private String path;

    @Override
    public List<FileDto> storageFiles(MultipartFile[] multipartFiles, String boardType) throws IOException {
        List<FileDto> savedFileList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {

                FileDto fileDto = FileDto.builder()
                        .boardType(boardType)
                        .originalName(multipartFile.getOriginalFilename())
                        .physicalName(UUID.randomUUID().toString())
                        .filePath("/" + boardType)
                        .extension(MultipartFileUtils.extractExtension(multipartFile))
                        .size(multipartFile.getSize())
                        .build();

                String filePath = path + boardType + "/" + fileDto.getPhysicalName() + "." + fileDto.getExtension();
                File saveFile = new File(filePath);

                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), saveFile);

                savedFileList.add(fileDto);

            }
        }

        return savedFileList;
    }
}

package com.admin.backend.service;

import com.admin.backend.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StorageService {

    List<FileDto> storageFiles(MultipartFile[] multipartFiles, String boardType) throws IOException;
}

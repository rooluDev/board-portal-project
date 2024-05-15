package com.admin.backend.common.validator;

import com.admin.backend.common.exception.IllegalFileDataException;
import com.admin.backend.common.utils.MultipartFileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public interface FileValidator<T> {

    void validateFile(MultipartFile[] files);

    void validateFileForModify(MultipartFile[] files, List<Long> deletedFileId, int currentFileSize);

    default void validateFileExtension(MultipartFile file, String[] allowedExtension) {
        boolean isAllowedExtension = Arrays.stream(allowedExtension).
                anyMatch(extension -> extension.equalsIgnoreCase(MultipartFileUtils.extractExtension(file)));

        if (!isAllowedExtension) {
            throw new IllegalFileDataException(file.getOriginalFilename() + "의 확장자를 확인하세요.");
        }
    }

    default void validateFileSize(MultipartFile file, int maxSize) {
        if (file.getSize() > maxSize) {
            throw new IllegalFileDataException(file.getOriginalFilename() + "의 크기를 확인하세요.");
        }
    }

    default void validateFilesLength(MultipartFile[] files, int maxFileLength) {
        if (files.length > maxFileLength) {
            throw new IllegalFileDataException("파일은 5개까지 등록 가능합니다.");
        }
    }

    default void validateFilesLength(MultipartFile[] files, List<Long> deletedFileId, int currentFileSize, int maxFileLength) {
        if (currentFileSize + files.length - deletedFileId.size() > maxFileLength) {
            throw new IllegalFileDataException("파일은 5개까지 등록 가능합니다.");
        }
    }
}

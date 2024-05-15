package com.user.backend.common.validator;

import com.user.backend.common.exception.custom.IllegalFileDataException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.common.utils.MultipartFileUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * Multipart File Validator
 *
 * @param <T> 제약조건
 */
public interface MultipartFileValidator<T> {

    /**
     * 제약조건을 체크
     *
     * @param files 체크할 멀티파트파일
     */
    void validateFile(MultipartFile[] files);

    void validateFileForModify(MultipartFile[] files, List<Long> deletedFileId, int currentFileSize);

    default void validateFileExtension(MultipartFile file, String[] allowedExtension) {
        boolean isAllowedExtension = Arrays.stream(allowedExtension).
                anyMatch(extension -> extension.equalsIgnoreCase(MultipartFileUtils.extractExtension(file)));

        if (!isAllowedExtension) {
            throw new IllegalFileDataException(ErrorCode.ILLEGAL_FILE_DATA);
        }
    }

    default void validateFileSize(MultipartFile file, DataSize maxSize) {
        if (file.getSize() > maxSize.toBytes()) {
            throw new IllegalFileDataException(ErrorCode.ILLEGAL_FILE_DATA);
        }
    }

    default void validateFilesLength(MultipartFile[] files, int maxFileLength) {
        if (files.length > maxFileLength) {
            throw new IllegalFileDataException(ErrorCode.ILLEGAL_FILE_DATA);
        }
    }

    default void validateFilesLength(MultipartFile[] files, List<Long> deletedFileId, int currentFileSize, int maxFileLength) {
        if (currentFileSize + files.length - deletedFileId.size() > maxFileLength) {
            throw new IllegalFileDataException(ErrorCode.ILLEGAL_FILE_DATA);
        }
    }
}

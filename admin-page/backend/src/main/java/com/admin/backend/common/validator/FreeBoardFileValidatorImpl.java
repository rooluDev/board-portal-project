package com.admin.backend.common.validator;

import com.admin.backend.common.validator.constraint.FreeBoardFileConstraint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * FreeBoardFileValidator Impl
 */
@Component
@RequiredArgsConstructor
public class FreeBoardFileValidatorImpl implements FileValidator<FreeBoardFileConstraint> {

    private final FreeBoardFileConstraint constraint;

    @Override
    public void validateFile(MultipartFile[] files) {
        this.validateFilesLength(files,constraint.getFileMinLength(), constraint.getFileMaxLength());

        for (MultipartFile file : files) {
            this.validateFileExtension(file, constraint.getAllowedExtension());
            this.validateFileSize(file, constraint.getMaxSize());
        }
    }

    @Override
    public void validateFileForModify(MultipartFile[] files, List<Long> deletedFileId, int currentFileSize) {
        if (files == null) {
            files = new MultipartFile[0];
        }
        if (deletedFileId == null) {
            deletedFileId = List.of();
        }

        this.validateFilesLength(files, deletedFileId, currentFileSize, constraint.getFileMinLength(), constraint.getFileMaxLength());

        for (MultipartFile file : files) {
            this.validateFileSize(file, constraint.getMaxSize());
            this.validateFileExtension(file, constraint.getAllowedExtension());
        }
    }
}

package com.admin.backend.common.validator;

import com.admin.backend.common.exception.IllegalFileDataException;
import com.admin.backend.common.validator.constraint.GalleryBoardFileConstraint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GalleryBoardFileValidatorImpl implements FileValidator<GalleryBoardFileConstraint> {

    private final GalleryBoardFileConstraint constraint;

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

        this.validateFilesLength(files, deletedFileId, currentFileSize,constraint.getFileMinLength() ,constraint.getFileMaxLength());

        for (MultipartFile file : files) {
            this.validateFileExtension(file, constraint.getAllowedExtension());
            this.validateFileSize(file, constraint.getMaxSize());
        }

    }
}

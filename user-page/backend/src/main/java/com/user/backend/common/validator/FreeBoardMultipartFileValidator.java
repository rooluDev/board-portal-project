package com.user.backend.common.validator;

import com.user.backend.common.exception.custom.IllegalFileDataException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.common.validator.constraint.FreeBoardFileConstraint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class FreeBoardMultipartFileValidator implements MultipartFileValidator<FreeBoardFileConstraint> {

    private final FreeBoardFileConstraint constraint;

    @Override
    public void validateFile(MultipartFile[] files) {
        this.validateFilesLength(files, constraint.getFileLength());

        for (MultipartFile file : files) {
            this.validateFileExtension(file, constraint.getAllowedExtension());
            this.validateFileSize(file, constraint.getMaxSize());
        }
    }

    @Override
    public void validateFileForModify(MultipartFile[] files, List<Long> deletedFileId, int currentFileSize) {
        if (files != null) {
            this.validateFilesLength(files, deletedFileId, currentFileSize, constraint.getFileLength());
            for (MultipartFile file : files) {
                this.validateFileExtension(file, constraint.getAllowedExtension());
                this.validateFileSize(file, constraint.getMaxSize());
            }
        } else if(deletedFileId != null) {
            if (currentFileSize == deletedFileId.size()) {
                throw new IllegalFileDataException(ErrorCode.ILLEGAL_FILE_DATA);
            }
        }
    }
}

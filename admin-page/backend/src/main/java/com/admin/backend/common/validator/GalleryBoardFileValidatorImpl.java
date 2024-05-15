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
        this.validateFilesLength(files, constraint.getFileLength());
        for (MultipartFile file : files) {
            this.validateFileExtension(file, constraint.getAllowedExtension());
            this.validateFileSize(file, constraint.getMaxSize());
        }
    }

    @Override
    public void validateFileForModify(MultipartFile[] files, List<Long> deletedFileId, int currentFileSize) {
        if(files != null){
            this.validateFilesLength(files, deletedFileId, currentFileSize, constraint.getFileLength());
            for (MultipartFile file : files) {
                this.validateFileExtension(file, constraint.getAllowedExtension());
                this.validateFileSize(file, constraint.getMaxSize());
            }
        }else {
            if(currentFileSize == deletedFileId.size()){
                throw new IllegalFileDataException("최소 1개의 파일을 등록해야합니다.");
            }
        }
    }
}

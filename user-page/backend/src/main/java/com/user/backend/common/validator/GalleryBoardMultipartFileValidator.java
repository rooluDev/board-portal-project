package com.user.backend.common.validator;

import com.user.backend.common.validator.constraint.GalleryBoardFileConstraint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * GalleryBoardMultipartFileValidator Impl
 */
@Component
@RequiredArgsConstructor
public class GalleryBoardMultipartFileValidator implements MultipartFileValidator<GalleryBoardFileConstraint> {

    private final GalleryBoardFileConstraint constraint;

    /**
     * 갤러리게시판 첨부파일 추가 시 검증 (파일 개수, 확장자, 크기)
     *
     * @param files 추가할 파일 배열
     */
    @Override
    public void validateFile(MultipartFile[] files) {

        this.validateFilesLength(files,constraint.getFileMinLength(), constraint.getFileMaxLength());

        for (MultipartFile file : files) {
            this.validateFileExtension(file, constraint.getAllowedExtension());
            this.validateFileSize(file, constraint.getMaxSize());
        }
    }

    /**
     * 갤러리게시판 첨부파일 수정 시 검증 (파일 개수, 확장자, 크기)
     *
     * @param files           추가할 파일 배열 (null 가능)
     * @param deletedFileId   삭제할 파일 ID 리스트 (null 가능)
     * @param currentFileSize 현재 게시물에 등록된 파일 개수
     */
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

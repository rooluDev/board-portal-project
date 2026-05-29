package com.admin.backend.common.validator;

import com.admin.backend.common.validator.constraint.GalleryBoardFileConstraint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * GalleryBoardFileValidator Impl
 */
@Component
@RequiredArgsConstructor
public class GalleryBoardFileValidatorImpl implements FileValidator<GalleryBoardFileConstraint> {

    private final GalleryBoardFileConstraint constraint;

    /**
     * 갤러리게시판 첨부파일 추가 시 유효성 검증 (파일 수, 확장자, 크기)
     *
     * @param files 추가할 파일 배열
     * @throws com.admin.backend.common.exception.IllegalFileDataException 유효성 검증 실패 시 발생
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
     * 갤러리게시판 첨부파일 수정 시 유효성 검증 (파일 수, 확장자, 크기)
     *
     * @param files           추가할 파일 배열 (null 가능)
     * @param deletedFileId   삭제할 파일의 pk 리스트 (null 가능)
     * @param currentFileSize 현재 게시물에 등록된 파일의 수
     * @throws com.admin.backend.common.exception.IllegalFileDataException 유효성 검증 실패 시 발생
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

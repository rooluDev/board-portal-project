package com.user.backend.common.validator;

import com.user.backend.common.validator.constraint.FreeBoardFileConstraint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * MultipartFileValidator Impl
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FreeBoardMultipartFileValidator implements MultipartFileValidator<FreeBoardFileConstraint> {

    private final FreeBoardFileConstraint constraint;

    /**
     * 자유게시판 첨부파일 추가 시 검증 (파일 개수, 확장자, 크기)
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
     * 자유게시판 첨부파일 수정 시 검증 (파일 개수, 확장자, 크기)
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

        this.validateFilesLength(files, deletedFileId, currentFileSize, constraint.getFileMinLength(), constraint.getFileMaxLength());

        for (MultipartFile file : files) {
            this.validateFileSize(file, constraint.getMaxSize());
            this.validateFileExtension(file, constraint.getAllowedExtension());
        }
    }
}

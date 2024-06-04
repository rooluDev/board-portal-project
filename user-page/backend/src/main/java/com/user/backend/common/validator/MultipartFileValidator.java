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
     * 게시판 첨부파일 추가 시 검증
     *
     * @param files 추가 할 파일
     */
    void validateFile(MultipartFile[] files);

    /**
     * 게시판 첨부파일 수정 시 검증
     *
     * @param files           추가 할 파일
     * @param deletedFileId   삭제 할 파일
     * @param currentFileSize 현재 파일의 수
     */
    void validateFileForModify(MultipartFile[] files, List<Long> deletedFileId, int currentFileSize);

    /**
     * 확장자 검증
     *
     * @param file             추가할 파일
     * @param allowedExtension 확장자 제약 조건
     */
    default void validateFileExtension(MultipartFile file, String[] allowedExtension) {
        boolean isAllowedExtension = Arrays.stream(allowedExtension).
                anyMatch(extension -> extension.equalsIgnoreCase(MultipartFileUtils.extractExtension(file)));

        if (!isAllowedExtension) {
            throw new IllegalFileDataException(ErrorCode.ILLEGAL_FILE_DATA);
        }
    }

    /**
     * 사이즈 검증
     *
     * @param file    추가할 파일
     * @param maxSize 최대 크기
     */
    default void validateFileSize(MultipartFile file, DataSize maxSize) {
        if (file.getSize() > maxSize.toBytes()) {
            throw new IllegalFileDataException(ErrorCode.ILLEGAL_FILE_DATA);
        }
    }

    /**
     * 게시판 추가 시 파일의 수 검증
     *
     * @param files         추가할 파일 리스트
     * @param minFileLength 파일의 최소 개수
     * @param maxFileLength 파일의 최대 개수
     */
    default void validateFilesLength(MultipartFile[] files, int minFileLength, int maxFileLength) {
        if (files.length < minFileLength) {
            throw new IllegalFileDataException(ErrorCode.ILLEGAL_FILE_DATA);
        } else if (files.length > maxFileLength) {
            throw new IllegalFileDataException(ErrorCode.ILLEGAL_FILE_DATA);
        }
    }

    /**
     * 게시판 수정 시
     *
     * @param files           추가할 파일 리스트
     * @param deletedFileId   삭제할 파일 리스트
     * @param currentFileSize 현재 파일 개수
     * @param minFileLength   파일의 최소 개수
     * @param maxFileLength   파일의 최대 개수
     */
    default void validateFilesLength(MultipartFile[] files, List<Long> deletedFileId, int currentFileSize, int minFileLength, int maxFileLength) {
        boolean isValidMaxFileLength = currentFileSize + files.length - deletedFileId.size() <= maxFileLength;
        boolean isValidMinFileLength = currentFileSize + files.length - deletedFileId.size() >= minFileLength;

        if (!isValidMaxFileLength) {
            throw new IllegalFileDataException(ErrorCode.ILLEGAL_FILE_DATA);
        } else if (!isValidMinFileLength) {
            throw new IllegalFileDataException(ErrorCode.ILLEGAL_FILE_DATA);
        }
    }
}

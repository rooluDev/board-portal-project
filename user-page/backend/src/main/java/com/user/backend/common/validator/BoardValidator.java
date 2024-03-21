package com.user.backend.common.validator;

import com.user.backend.common.exception.IllegalBoardDataException;
import com.user.backend.common.exception.IllegalFileDataException;
import com.user.backend.common.utils.MultipartFileUtils;
import com.user.backend.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Board input data validator
 */
@Slf4j
public class BoardValidator {
    // TODO : ENUM?
    private static final int NOTICE_TITLE_MIN = 1;
    private static final int NOTICE_TITLE_MAX = 99;
    private static final int NOTICE_CONTENT_MIN = 1;
    private static final int NOTICE_CONTENT_MAX = 3999;

    private static final int FREE_TITLE_MIN = 1;
    private static final int FREE_TITLE_MAX = 99;
    private static final int FREE_CONTENT_MIN = 1;
    private static final int FREE_CONTENT_MAX = 3999;
    private static final int FREE_FILE_LENGTH = 5;

    private static final int GALLERY_TITLE_MIN = 1;
    private static final int GALLERY_TITLE_MAX = 99;
    private static final int GALLERY_CONTENT_MIN = 1;
    private static final int GALLERY_CONTENT_MAX = 3999;

    private static final int FILE_MAX_SIZE = 2 * 1024 * 1024;
    private static final List<String> ALLOWED_FILE_EXTENSION_FOR_FREE = List.of("jpg", "gif", "png", "zip");
    private static final List<String> ALLOWED_FILE_EXTENSION_FOR_GALLERY = List.of("jpg", "gif", "png", "zip");

    /**
     * Notice Board Validator
     *
     * @param noticeBoardDto 추가 또는 수정한 문의 게시판 데이터
     */
    public static void validateNoticeBoard(NoticeBoardDto noticeBoardDto) {
        validateText(noticeBoardDto.getTitle(), NOTICE_TITLE_MIN, NOTICE_TITLE_MAX);
        validateText(noticeBoardDto.getContent(), NOTICE_CONTENT_MIN, NOTICE_CONTENT_MAX);
    }

    /**
     * Free Board Add Validator
     *
     * @param freeBoardDto 추가한 자유 게시판 데이터
     */
    public static void validateFreeBoard(FreeBoardDto freeBoardDto) {
        validateText(freeBoardDto.getTitle(), FREE_TITLE_MIN, FREE_TITLE_MAX);
        validateText(freeBoardDto.getContent(), FREE_CONTENT_MIN, FREE_CONTENT_MAX);

        if (freeBoardDto.getFile() != null) {
            validateFileLength(freeBoardDto.getFile().length, FREE_FILE_LENGTH);
            validateFileExtension(freeBoardDto.getFile(), ALLOWED_FILE_EXTENSION_FOR_FREE);
            validateFileSize(freeBoardDto.getFile(), FILE_MAX_SIZE);
        }
    }

    /**
     * Free Board Modify Validator
     *
     * @param freeBoardDto     수정한 문의 게시판 데이터
     * @param alreadyFileCount 이미 게시판의 존재 했던 파일의 수
     */
    public static void validateFreeBoard(FreeBoardDto freeBoardDto, int alreadyFileCount) {
        validateText(freeBoardDto.getTitle(), FREE_TITLE_MIN, FREE_TITLE_MAX);
        validateText(freeBoardDto.getContent(), FREE_CONTENT_MIN, FREE_CONTENT_MAX);

        validateFileLength(alreadyFileCount, freeBoardDto.getFile(), freeBoardDto.getDeleteFileIdList());
        if (freeBoardDto.getFile() != null) {
            validateFileExtension(freeBoardDto.getFile(), ALLOWED_FILE_EXTENSION_FOR_FREE);
            validateFileSize(freeBoardDto.getFile(), FILE_MAX_SIZE);
        }
    }


    /**
     * Gallery Board Add Validator
     *
     * @param galleryBoardDto 추가한 자유 게시판 데이터
     */
    public static void validateGalleryBoard(GalleryBoardDto galleryBoardDto) {
        validateText(galleryBoardDto.getTitle(), GALLERY_TITLE_MIN, GALLERY_TITLE_MAX);
        validateText(galleryBoardDto.getContent(), GALLERY_CONTENT_MIN, GALLERY_CONTENT_MAX);

        if (galleryBoardDto.getFile() != null) {
            validateFileLength(galleryBoardDto.getFile().length, FREE_FILE_LENGTH);
        } else {
            throw new IllegalFileDataException("파일을 1개 이상 등록하세요.");
        }

        validateFileExtension(galleryBoardDto.getFile(), ALLOWED_FILE_EXTENSION_FOR_GALLERY);

        validateFileSize(galleryBoardDto.getFile(), FILE_MAX_SIZE);
    }


    /**
     * Gallery Board Modify Validator
     *
     * @param galleryBoardDto  수정한 갤러리 게시판 데이터
     * @param alreadyFileCount 이미 게시판의 존재 했던 파일의 수
     */
    public static void validateGalleryBoard(GalleryBoardDto galleryBoardDto, int alreadyFileCount) {
        validateText(galleryBoardDto.getContent(), GALLERY_CONTENT_MIN, GALLERY_CONTENT_MAX);
        validateText(galleryBoardDto.getTitle(), GALLERY_TITLE_MIN, GALLERY_TITLE_MAX);

        validateFileLength(alreadyFileCount, galleryBoardDto.getFile(), galleryBoardDto.getDeleteFileIdList());
        if (galleryBoardDto.getFile() != null) {
            validateFileExtension(galleryBoardDto.getFile(), ALLOWED_FILE_EXTENSION_FOR_FREE);
            validateFileSize(galleryBoardDto.getFile(), FILE_MAX_SIZE);
        }
    }

    private static void validateText(String text, int minLength, int maxLength) {
        if (text == null) {
            throw new IllegalBoardDataException("입력칸이 비어있습니다.");
        } else if (text.length() < minLength || text.length() > maxLength) {
            throw new IllegalBoardDataException("입력값이 올바르지 않습니다.");
        }
    }

    private static void validateFileLength(int fileLength, int maxLength) {
        if (fileLength > maxLength) {
            throw new IllegalFileDataException("업로드 파일은 5개까지 가능합니다.");
        }
    }

    private static void validateFileLength(int fileCountInBoard, MultipartFile[] multipartFiles, List<Long> deleteFileList) {
        if (multipartFiles == null) {
            return;
        }
        if (deleteFileList == null) {
            if (fileCountInBoard + multipartFiles.length > 5) {
                throw new IllegalFileDataException("업로드 파일은 5개까지 가능합니다.");
            }
        } else {
            if (fileCountInBoard - deleteFileList.size() + multipartFiles.length > 5) {
                throw new IllegalFileDataException("업로드 파일은 5개까지 가능합니다.");
            }
        }
    }

    private static void validateFileSize(MultipartFile[] fileList, int maxSize) {
        for (MultipartFile file : fileList) {
            if (file.getSize() > maxSize) {
                throw new IllegalFileDataException("파일 사이즈가 " + maxSize + "보다 크면 안됩니다.");
            }
        }
    }

    private static void validateFileExtension(MultipartFile[] fileList, List<String> allowedExtension) {
        for (MultipartFile file : fileList) {
            if (!allowedExtension.contains(MultipartFileUtils.extractExtension(file))) {
                throw new IllegalFileDataException("jpg, gif, png, zip 파일만 가능합니다.");
            }
        }
    }
}

package com.admin.backend.common.validator;

import com.admin.backend.common.exception.IllegalBoardDataException;
import com.admin.backend.dto.NoticeBoardDto;

/**
 * Board input data validator
 */
public class BoardValidator {

    private static final int NOTICE_TITLE_MIN = 1;
    private static final int NOTICE_TITLE_MAX = 99;
    private static final int NOTICE_CONTENT_MIN = 1;
    private static final int NOTICE_CONTENT_MAX = 3999;

    /**
     * Notice Board Validator
     *
     * @param noticeBoardDto
     */
    public static void validateNoticeBoard(NoticeBoardDto noticeBoardDto) {
        validateText(noticeBoardDto.getTitle(), NOTICE_TITLE_MIN, NOTICE_TITLE_MAX);
        validateText(noticeBoardDto.getContent(), NOTICE_CONTENT_MIN, NOTICE_CONTENT_MAX);
    }

    private static void validateText(String text, int minLength, int maxLength) {
        if (text == null) {
            throw new IllegalBoardDataException("입력칸이 비어있습니다.");
        } else if (text.length() < minLength || text.length() > maxLength) {
            throw new IllegalBoardDataException("입력값이 올바르지 않습니다.");
        }
    }
}

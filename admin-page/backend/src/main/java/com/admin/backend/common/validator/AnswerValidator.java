package com.admin.backend.common.validator;

import com.admin.backend.common.exception.IllegalAnswerDataException;

/**
 * Answer Validator
 */
public class AnswerValidator {

    private static final int ANSWER_CONTENT_MAX = 3999;

    /**
     * validate Answer
     *
     * @param content
     */
    public static void validateAnswer(String content) {
        validateText(content, 0, ANSWER_CONTENT_MAX);
    }

    private static void validateText(String text, int minLength, int maxLength) {
        if (text == null) {
            throw new IllegalAnswerDataException("입력칸이 비어있습니다.");
        } else if (text.length() < minLength || text.length() > maxLength) {
            throw new IllegalAnswerDataException("입력값이 올바르지 않습니다.");
        }
    }
}

package com.user.backend.common.exception;

/**
 * 답변 등록 시 데이터 검증이 실패 할 시 발생하는 Exception
 */
public class IllegalAnswerDataException extends RuntimeException{
    public IllegalAnswerDataException(String message) {
        super(message);
    }
}

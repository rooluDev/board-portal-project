package com.admin.backend.common.exception;

/**
 * 답변 등록 시 데이터 검증이 실패 할 시 발생하는 Exception
 */
public class IllegalAnswerDataException extends RuntimeException{

    /**
     * 에러 메시지를 포함한 생성자
     *
     * @param message 에러 메시지
     */
    public IllegalAnswerDataException(String message) {
        super(message);
    }
}

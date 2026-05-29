package com.admin.backend.common.exception;

/**
 * 상단 고정 게시물을 추가 또는 수정 시 5개 이상일 시 발생하는 Exception
 */
public class FixedBoardFullException extends RuntimeException{

    /**
     * 에러 메시지를 포함한 생성자
     *
     * @param message 에러 메시지
     */
    public FixedBoardFullException(String message) {
        super(message);
    }
}

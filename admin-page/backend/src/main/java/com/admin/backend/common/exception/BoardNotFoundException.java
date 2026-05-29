package com.admin.backend.common.exception;

/**
 * 요청한 게시물이 없을 경우 발생하는 Exception
 */
public class BoardNotFoundException extends RuntimeException{

    /**
     * 기본 생성자
     */
    public BoardNotFoundException() {
        super();
    }

    /**
     * 에러 메시지를 포함한 생성자
     *
     * @param message 에러 메시지
     */
    public BoardNotFoundException(String message) {
        super(message);
    }
}

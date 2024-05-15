package com.admin.backend.common.exception;

/**
 * 요청한 게시물이 없을 경우 발생하는 Exception
 */
public class BoardNotFoundException extends RuntimeException{

    public BoardNotFoundException() {
        super();
    }

    public BoardNotFoundException(String message) {
        super(message);
    }
}

package com.admin.backend.common.exception;

/**
 * 댓글이 없을 때 발생하는 Exception
 */
public class CommentNotFoundException extends RuntimeException{

    /**
     * 에러 메시지를 포함한 생성자
     *
     * @param message 에러 메시지
     */
    public CommentNotFoundException(String message) {
        super(message);
    }
}

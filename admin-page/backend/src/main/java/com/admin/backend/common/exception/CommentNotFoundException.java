package com.admin.backend.common.exception;

/**
 * 댓글이 없을 때 발생하는 Exception
 */
public class CommentNotFoundException extends RuntimeException{

    public CommentNotFoundException(String message) {
        super(message);
    }
}

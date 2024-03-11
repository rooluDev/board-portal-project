package com.admin.backend.common.exception;

/**
 * 상단 고정인 게시물이 5개 이상일 시 발생하는 Exception
 */
public class FixedBoardFullException extends RuntimeException{
    public FixedBoardFullException(String message) {
        super(message);
    }
}

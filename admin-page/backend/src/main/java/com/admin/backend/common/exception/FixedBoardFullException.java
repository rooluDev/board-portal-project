package com.admin.backend.common.exception;

/**
 * 상단 고정 게시물을 추가 또는 수정 시 5개 이상일 시 발생하는 Exception
 */
public class FixedBoardFullException extends RuntimeException{
    public FixedBoardFullException(String message) {
        super(message);
    }
}

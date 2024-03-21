package com.user.backend.common.exception;

/**
 * 게시물 저장 또는 수정 시 데이터 검증 실패 시 발생하는 Exception
 */
public class IllegalBoardDataException extends RuntimeException{
    public IllegalBoardDataException(String message) {
        super(message);
    }
}

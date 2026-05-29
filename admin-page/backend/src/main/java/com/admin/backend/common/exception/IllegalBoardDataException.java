package com.admin.backend.common.exception;

/**
 * 게시물 저장 또는 수정 시 데이터 검증 실패 시 발생하는 Exception
 */
public class IllegalBoardDataException extends RuntimeException{

    /**
     * 에러 메시지를 포함한 생성자
     *
     * @param message 에러 메시지
     */
    public IllegalBoardDataException(String message) {
        super(message);
    }
}

package com.admin.backend.common.exception;

/**
 * File 업로드 정책을 어길 시 발생하는 Exception
 */
public class IllegalFileDataException extends RuntimeException{

    /**
     * 에러 메시지를 포함한 생성자
     *
     * @param message 에러 메시지
     */
    public IllegalFileDataException(String message) {
        super(message);
    }
}

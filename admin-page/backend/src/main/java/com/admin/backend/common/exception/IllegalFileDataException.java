package com.admin.backend.common.exception;

/**
 * File 업로드 정책을 어길 시 발생하는 Exception
 */
public class IllegalFileDataException extends RuntimeException{
    public IllegalFileDataException(String message) {
        super(message);
    }
}

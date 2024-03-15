package com.admin.backend.common.exception;

/**
 * File 제약사항 어길시 발생하는 Exception
 */
public class IllegalFileDataException extends RuntimeException{
    public IllegalFileDataException(String message) {
        super(message);
    }
}

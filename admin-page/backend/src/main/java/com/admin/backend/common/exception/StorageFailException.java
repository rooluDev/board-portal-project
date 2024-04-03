package com.admin.backend.common.exception;

/**
 * 파일 저장 실패시 발생하는 Exception
 */
public class StorageFailException extends RuntimeException{
    public StorageFailException(String message) {
        super(message);
    }
}

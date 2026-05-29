package com.admin.backend.common.exception;

/**
 * 파일 저장 실패시 발생하는 Exception
 */
public class StorageFailException extends RuntimeException{

    /**
     * 에러 메시지를 포함한 생성자
     *
     * @param message 에러 메시지
     */
    public StorageFailException(String message) {
        super(message);
    }

    /**
     * 기본 생성자
     */
    public StorageFailException() {
        super();
    }
}

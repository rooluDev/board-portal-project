package com.admin.backend.common.exception;

/**
 * 검색 조건 정책을 어길 시 발생하는 Exception
 */
public class IllegalSearchConditionDataException extends RuntimeException{

    /**
     * 에러 메시지를 포함한 생성자
     *
     * @param message 에러 메시지
     */
    public IllegalSearchConditionDataException(String message) {
        super(message);
    }
}

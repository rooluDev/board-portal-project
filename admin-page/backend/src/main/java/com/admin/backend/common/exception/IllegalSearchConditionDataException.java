package com.admin.backend.common.exception;

/**
 * 검색 조건 정책을 어길 시 발생하는 Exception
 */
public class IllegalSearchConditionDataException extends RuntimeException{
    public IllegalSearchConditionDataException(String message) {
        super(message);
    }
}

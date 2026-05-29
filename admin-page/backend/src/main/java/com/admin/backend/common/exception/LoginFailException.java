package com.admin.backend.common.exception;

/**
 * 로그인 실패시 발생하는 Exception
 */
public class LoginFailException extends RuntimeException{

    /**
     * 에러 메시지를 포함한 생성자
     *
     * @param message 에러 메시지
     */
    public LoginFailException(String message) {
        super(message);
    }
}

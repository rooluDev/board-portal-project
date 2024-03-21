package com.user.backend.common.exception;

/**
 * 로그인 실패시 발생하는 Exception
 */
public class LoginFailException extends RuntimeException{
    public LoginFailException(String message) {
        super(message);
    }
}

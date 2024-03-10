package com.admin.backend.common.exception;

/**
 * Login Fail Exception
 */
public class LoginFailException extends RuntimeException{
    public LoginFailException(String message) {
        super(message);
    }
}

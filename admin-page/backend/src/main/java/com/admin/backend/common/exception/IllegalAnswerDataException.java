package com.admin.backend.common.exception;

/**
 * Answer 4000자 넘어갈시 발생하는 Exception
 */
public class IllegalAnswerDataException extends RuntimeException{
    public IllegalAnswerDataException(String message) {
        super(message);
    }
}

package com.user.backend.common.exception.custom;

import com.user.backend.common.exception.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원가입 실패시 발생하는 Exception
 */
@AllArgsConstructor
@Getter
public class JoinFailException extends RuntimeException{
    ErrorCode errorCode;
}

package com.user.backend.common.exception.custom;

import com.user.backend.common.exception.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 아이디 중복 검사시 중복일 때 발생하는 Exception
 */
@AllArgsConstructor
@Getter
public class MemberIdExistedException extends RuntimeException{
    ErrorCode errorCode;
}

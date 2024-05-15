package com.user.backend.common.exception.custom;

import com.user.backend.common.exception.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 요청한 Member가 없을 경우 발생하는 Exception
 */
@AllArgsConstructor
@Getter
public class MemberNotFoundException extends RuntimeException{
    ErrorCode errorCode;
}

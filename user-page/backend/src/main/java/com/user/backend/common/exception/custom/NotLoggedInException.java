package com.user.backend.common.exception.custom;

import com.user.backend.common.exception.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인이 안되어있을 때 사용할 수 없는 API를 사용 시 발생하는 Exception
 */
@AllArgsConstructor
@Getter
public class NotLoggedInException extends RuntimeException{
    ErrorCode errorCode;
}

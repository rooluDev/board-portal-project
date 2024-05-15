package com.user.backend.common.exception.custom;

import com.user.backend.common.exception.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 요청한 게시물이 없을 경우 발생하는 Exception
 */
@AllArgsConstructor
@Getter
public class BoardNotFoundException extends RuntimeException{
    ErrorCode errorCode;
}

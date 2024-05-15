package com.user.backend.common.exception.custom;

import com.user.backend.common.exception.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 작성자가 본인이 아닐경우 발생하는 Exception
 */
@AllArgsConstructor
@Getter
public class NotMyBoardException extends RuntimeException{
    ErrorCode errorCode;
}

package com.user.backend.common.exception.custom;

import com.user.backend.common.exception.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * File 업로드 정책을 어길 시 발생하는 Exception
 */
@AllArgsConstructor
@Getter
public class IllegalFileDataException extends RuntimeException {
    ErrorCode errorCode;
}

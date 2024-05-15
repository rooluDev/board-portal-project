package com.user.backend.common.exception.custom;

import com.user.backend.common.exception.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 파일 저장에 실패할 때 발생하는 Exception
 */
@AllArgsConstructor
@Getter
public class StorageFailException extends RuntimeException {
    ErrorCode errorCode;
}

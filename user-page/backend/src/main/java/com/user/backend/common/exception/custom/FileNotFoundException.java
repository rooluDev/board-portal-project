package com.user.backend.common.exception.custom;

import com.user.backend.common.exception.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 파일이 없을 때 발생하는 Exception
 */
@AllArgsConstructor
@Getter
public class FileNotFoundException extends RuntimeException {
    ErrorCode errorCode;
}

package com.user.backend.common.exception.custom;

import com.user.backend.common.exception.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 파일 리소스 다운 실패시 발생하는 Exception
 */
@AllArgsConstructor
@Getter
public class DownloadFailException extends RuntimeException {
    ErrorCode errorCode;
}

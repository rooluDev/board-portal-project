package com.user.backend.common.exception.custom;

import com.user.backend.common.exception.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DB에서 썸네일을 가져올 때 썸네일이 없을 경우 발생하는 Exception
 */
@AllArgsConstructor
@Getter
public class ThumbnailNotFoundException extends RuntimeException{
    ErrorCode errorCode;
}

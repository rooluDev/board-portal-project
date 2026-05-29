package com.user.backend.common.exception.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

/**
 * Custom Error Response
 * {
 * "status": ,
 * "name": ,
 * "code": ,
 * "message":
 * }
 */
@Getter
@Builder
public class ErrorResponseEntity {
    private int status;
    private String name;
    private String code;
    private String message;

    /**
     * ErrorCode를 기반으로 ResponseEntity 생성
     *
     * @param e 응답에 사용할 ErrorCode
     * @return ErrorResponseEntity를 담은 ResponseEntity
     */
    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .status(e.getHttpStatus().value())
                        .name(e.name())
                        .code(e.getCode())
                        .message(e.getMessage())
                        .build());
    }
}

package com.admin.backend.common.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * BindingResult 유틸리티 클래스
 */
public class BindingResultUtils {

    /**
     * BindingResult에서 지정된 필드들의 에러 메시지를 순서대로 탐색하여 첫 번째 에러 메시지 반환
     *
     * @param bindingResult 유효성 검증 결과
     * @param fields        에러를 탐색할 필드명 배열
     * @return 첫 번째로 발견된 필드 에러의 기본 메시지, 없으면 null
     */
    public static String getErrorMessage(BindingResult bindingResult, String[] fields) {
        for (String field : fields) {
            if (bindingResult.hasFieldErrors(field)) {
                FieldError error = bindingResult.getFieldError(field);
                if (error != null) {
                    return error.getDefaultMessage();
                }
            }
        }
        return null;
    }
}

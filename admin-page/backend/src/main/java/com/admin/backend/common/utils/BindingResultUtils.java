package com.admin.backend.common.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class BindingResultUtils {

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

package com.admin.backend.common.validator.constraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

/**
 * FreeBoardFileConstraint
 */
@Getter
@NoArgsConstructor
@Component
public class FreeBoardFileConstraint {

    @Value("#{constraint['free.file.allowed.extension']}")
    private String[] allowedExtension;

    @Value("#{constraint['free.file.allowed.max.size']}")
    private DataSize maxSize;

    @Value("#{constraint['free.file.allowed.minLength']}")
    private int fileMinLength;

    @Value("#{constraint['free.file.allowed.maxLength']}")
    private int fileMaxLength;
}

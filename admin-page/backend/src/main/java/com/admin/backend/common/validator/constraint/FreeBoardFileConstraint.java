package com.admin.backend.common.validator.constraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Getter
@NoArgsConstructor
@Component
public class FreeBoardFileConstraint {

    @Value("#{constraint['free.file.allowed.extension']}")
    private String[] allowedExtension;

    @Value("#{constraint['free.file.allowed.max.size']}")
    private int maxSize;

    @Value("#{constraint['free.file.allowed.length']}")
    private int fileLength;
}

package com.admin.backend.common.validator.constraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@NoArgsConstructor
@Component
public class GalleryBoardFileConstraint {

    @Value("#{constraint['gallery.file.allowed.extension']}")
    private String[] allowedExtension;

    @Value("#{constraint['gallery.file.allowed.max.size']}")
    private int maxSize;

    @Value("#{constraint['gallery.file.allowed.length']}")
    private int fileLength;
}

package com.user.backend.common.validator.constraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

@Getter
@Component
@NoArgsConstructor
public class GalleryBoardFileConstraint {

    @Value("#{constraint['gallery.file.allowed.extension']}")
    private String[] allowedExtension;

    @Value("#{constraint['gallery.file.allowed.max.size']}")
    private DataSize maxSize;

    @Value("#{constraint['gallery.file.allowed.length']}")
    private int fileLength;
}

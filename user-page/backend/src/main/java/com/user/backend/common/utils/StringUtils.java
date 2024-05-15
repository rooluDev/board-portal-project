package com.user.backend.common.utils;

import com.user.backend.dto.FileDto;
import com.user.backend.dto.ThumbnailDto;

/**
 * Custom String Utils
 */
public class StringUtils {

    /**
     * ThumbnailDto를 매개변수로 받아 상대경로로 반환
     *
     * @param thumbnailDto ThumbnailDto
     * @return 상대경로 ex : /thumbnail/physicalName.png
     */
    public static String parseToPath(ThumbnailDto thumbnailDto) {
        return thumbnailDto.getFilePath() + "/" + thumbnailDto.getPhysicalName() + "." + thumbnailDto.getExtension();
    }

    /**
     * FileDto를 매개변수로 받아 상대경로로 반환
     * @param fileDto FileDto
     * @return 상대경로 ex : /gallery/physicalName.png
     */
    public static String parseToPath(FileDto fileDto) {
        return fileDto.getFilePath() + "/" + fileDto.getPhysicalName() + "." + fileDto.getExtension();
    }
}

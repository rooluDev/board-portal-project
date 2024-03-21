package com.user.backend.common.utils;

import org.springframework.web.multipart.MultipartFile;

/**
 * Custom Multipart File Util class
 */
public class MultipartFileUtils {

    /**
     * original name에서 확장자 추출
     *
     * @param file 저장 할 MultipartFile
     * @return 파일의 확장자
     */
    public static String extractExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            int lastDotIndex = fileName.lastIndexOf('.');
            if (lastDotIndex > 0) {
                return fileName.substring(lastDotIndex + 1);
            }
        }
        return null;
    }
}

package com.admin.backend.common.utils;

import org.springframework.web.multipart.MultipartFile;

/**
 * Custom Multipart File Util class
 */
public class MultipartFileUtils {

    /**
     * original name에서 확장자 추출
     *
     * @param file
     * @return
     */
    public static String extractExtension(MultipartFile file){
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

package com.admin.backend.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Multipart 내부 빈 파일 삭제 후 return
     *
     * @param fileList 전의 파일 리스트
     * @return 내부 빈 파일을 삭제한 파일 리스트
     */
    public static MultipartFile[] replaceEmptyFile(MultipartFile[] fileList) {
        if(fileList == null){
            return null;
        }
        List<MultipartFile> replacedFileList = new ArrayList<>();
        for (MultipartFile file : fileList) {
            if (file != null && !file.isEmpty()) {
                replacedFileList.add(file);
            }
        }
        // 리스트가 비어있으면 null 반환
        if (replacedFileList.isEmpty()) {
            return null;
        }
        return replacedFileList.toArray(new MultipartFile[replacedFileList.size()]);
    }

}
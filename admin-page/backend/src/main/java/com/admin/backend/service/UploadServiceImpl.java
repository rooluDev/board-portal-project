package com.admin.backend.service;

import com.admin.backend.dto.FileDto;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Upload Service 구현체
 */
@Service
@Primary
public class UploadServiceImpl implements UploadService {

    public static final String PATH = "/Users/user/workspace/eBrain-study/board-protal/admin-page/backend/src/main/resources/static/upload/";

    @Override
    public void uploadFile(List<FileDto> fileDtoList, MultipartFile[] fileList, String boardType) throws IOException {
        // TODO : index 무결성
        for (int i = 0; i < fileList.length; i++) {
            if (!fileList[i].isEmpty()) {
                FileDto fileDto = fileDtoList.get(i);
                String filePath = PATH + boardType + "/" + fileDto.getPhysicalName() + "." + fileDto.getExtension();

                File uploadFile = new File(filePath);
                FileUtils.copyInputStreamToFile(fileList[i].getInputStream(), uploadFile);
            }
        }
    }
}

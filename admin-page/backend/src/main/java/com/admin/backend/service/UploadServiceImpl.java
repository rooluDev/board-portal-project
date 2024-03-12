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
public class UploadServiceImpl implements UploadService{

    @Override
    public void uploadFile(List<FileDto> fileDtoList, MultipartFile[] fileList) throws IOException {
        for(int i = 0; i<fileList.length;i++){
            if(!fileList[i].isEmpty()){
                FileDto fileDto = fileDtoList.get(i);
                String filePath = FileServiceImpl.PATH + fileDto.getPhysicalName() + "." + fileDto.getExtension();
                File uploadFile = new File(filePath);
                FileUtils.copyInputStreamToFile(fileList[i].getInputStream(),uploadFile);
            }
        }
    }
}

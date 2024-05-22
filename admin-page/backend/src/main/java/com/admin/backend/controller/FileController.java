package com.admin.backend.controller;


import com.admin.backend.common.exception.FileNotFoundException;
import com.admin.backend.dto.FileDto;
import com.admin.backend.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;

/**
 * File Controller
 */
@Controller
@RequiredArgsConstructor
public class FileController {

    @Value("#{storage['path']}")
    private String path;
    private final FileService fileService;


    /**
     * 파일 다운로드
     *
     * @param filId    PathVariable
     * @param response HttpServletResponse
     * @throws IOException IOException
     */
    @GetMapping("/file/download/{fileId}")
    public void downloadFile(@PathVariable(name = "fileId") Long filId, HttpServletResponse response) throws IOException {

        // 파일 데이터 가져오기
        FileDto fileDto = fileService.getFileById(filId).orElseThrow(() -> new FileNotFoundException("잘못된 요청입니다."));

        // 파일 객체 생성
        String fileName = fileDto.getPhysicalName() + "." + fileDto.getExtension();
        String filePath = path + fileDto.getFilePath() + "/" + fileName;
        File file = new File(filePath);

        // response 설정
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/octet-stream");

        // 다운로드
        download(file, response.getOutputStream());
    }


    private void download(File file, OutputStream outputStream) throws IOException {
        // try-with-resource 파일 다운로드
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             BufferedOutputStream bos = new BufferedOutputStream(outputStream)) {

            byte[] buffer = new byte[1024 * 1024 * 5];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        }
    }
}

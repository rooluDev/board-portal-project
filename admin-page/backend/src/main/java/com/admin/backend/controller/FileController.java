package com.admin.backend.controller;


import com.admin.backend.dto.FileDto;
import com.admin.backend.service.FileService;
import com.admin.backend.service.UploadServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;

/**
 * File Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class FileController {
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
        FileDto fileDto = fileService.getFileById(filId).orElseThrow(() -> new FileNotFoundException());

        // 파일 객체 생성
        String fileName = fileDto.getPhysicalName() + "." + fileDto.getExtension();
        String filePath = UploadServiceImpl.PATH + fileDto.getFilePath() + "/" + fileName;
        File file = new File(filePath);

        // response 설정
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/octet-stream");

        // 다운로드
        download(file, response.getOutputStream());
    }

    /**
     * 파일 다운로드
     *
     * @param file
     * @param outputStream
     * @throws IOException
     */
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

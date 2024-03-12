package com.admin.backend.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Free Board 추가 Dto
 */
@Getter
@Setter
public class FreeBoardAddDto {
    private Long categoryId;
    private String title;
    private String content;
    private MultipartFile[] file;
}

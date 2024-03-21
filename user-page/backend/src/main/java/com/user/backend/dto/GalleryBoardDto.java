package com.user.backend.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

/**
 * Gallery Board Dto
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class GalleryBoardDto {
    private Long boardId;
    private Long categoryId;
    private String authorType;
    private String authorId;
    private String title;
    private String content;
    private int views;
    private Timestamp createdAt;
    private Timestamp editedAt;
    private int deleted;
    private MultipartFile[] file;
    private List<Long> deleteFileIdList;
    private String categoryName; // list page에 필요한 카테고리 이름
    private String filePath; // list page 이미지 추출 시 필요한 파일 경로
    private String physicalName; // list page 이미지 추출 시 필요한 저장된 파일 이름
    private String extension; // list page 이미지 추출 시 필요한 확장자
    private String adminName; // list page에 필요한 등록자 이름
    private String memberName; // list page에 필요한 등록자 이름
    private int fileCount; // list page에 필요한 파일 수
}

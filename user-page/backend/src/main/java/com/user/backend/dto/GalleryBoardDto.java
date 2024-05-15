package com.user.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;

/**
 * Gallery Board Dto
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GalleryBoardDto {
    private Long boardId;
    private Long categoryId;
    private String authorType;
    private String authorId;
    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 99, message = "100자 미만으로 작성하세요")
    private String title;
    @NotBlank(message = "내용을 입력하세요.")
    @Size(max = 3999, message = "내용이 4000자 미만으로 작성하세요.")
    private String content;
    private int views;
    private Timestamp createdAt;
    private Timestamp editedAt;
    private int isDeleted;
    private String categoryName; // list page에 필요한 카테고리 이름
    private Long thumbnailId; // list page에 필요한 thumbnailId
    private String filePath; // list page 이미지 추출 시 필요한 파일 경로
    private String physicalName; // list page 이미지 추출 시 필요한 저장된 파일 이름
    private String extension; // list page 이미지 추출 시 필요한 확장자
    private String adminName; // list page에 필요한 등록자 이름
    private String memberName; // list page에 필요한 등록자 이름
    private int fileCount; // list page에 필요한 파일 수
}

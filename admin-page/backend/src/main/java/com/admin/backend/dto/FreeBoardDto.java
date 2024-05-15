package com.admin.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;

/**
 * Free Board Dto
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardDto {
    private Long boardId;
    private Long categoryId;
    private String authorType;
    private String authorId;
    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 99, message = "제목을 100자 미만으로 입력하세요.")
    private String title;
    @NotBlank(message = "내용을 입력하세요.")
    @Size(max = 3999, message = "내용을 4000자 미만으로 입력하세요.")
    private String content;
    private int views;
    private int isDeleted;
    private Timestamp createdAt;
    private Timestamp editedAt;
    private String categoryName; // list page에 필요한 카테고리 이름
    private Long fileId; // list page에 필요한 파일 여부를 위한 파일 아이디
    private String adminName; // list page에 필요한 등록자 이름
    private String memberName; // list page에 필요한 등록자 이름
    private int commentCount; // list page에 필요한 댓글 수
}

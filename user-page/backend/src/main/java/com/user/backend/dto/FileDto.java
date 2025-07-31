package com.user.backend.dto;

import lombok.*;

import java.sql.Timestamp;

/**
 * File Dto
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private Long fileId;
    private String boardType;
    private Long boardId;
    private String originalName;
    private String physicalName;
    private String filePath;
    private String extension;
    private Long size;
    private Timestamp createdAt;
    private Timestamp editedAt;
}

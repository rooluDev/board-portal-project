package com.admin.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * File Dto
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

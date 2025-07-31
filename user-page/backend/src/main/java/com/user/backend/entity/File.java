package com.user.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * File Entity
 */
@Entity
@Table(name = "tb_file")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "board_type", nullable = false, length = 10)
    private String boardType;

    @Column(name = "board_id",nullable = false)
    private Long boardId;

    @Column(name = "original_name", nullable = false, length = 100)
    private String originalName;

    @Column(name = "physical_name", nullable = false, length = 36)
    private String physicalName;

    @Column(name = "file_path", nullable = false, length = 15)
    private String filePath;

    @Column(name = "extension", nullable = false, length = 10)
    private String extension;

    @Column(name = "size", nullable = false)
    private Long size;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "edited_at", nullable = true)
    private Timestamp editedAt;
}

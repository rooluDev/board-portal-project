package com.user.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * Thumbnail Entity
 */
@Entity
@Table(name = "tb_thumbnail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Thumbnail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thumbnail_id")
    private Long thumbnailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id" ,nullable = false)
    private File file;

    @Column(name = "original_name", nullable = false, length = 100)
    private String originalName;

    @Column(name = "physical_name", nullable = false, length = 36)
    private String physicalName;

    @Column(name = "file_path", nullable = false, length = 15)
    private String filePath;

    @Column(nullable = false, length = 10)
    private String extension;

    @Column(nullable = false)
    private Long size;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "edited_at", nullable = true)
    private Timestamp editedAt;

}

package com.user.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * InquiryBoard Entity
 */
@Entity
@Table(name = "tb_inquiry_board")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @Column(name = "title", nullable = false, length = 99)
    private String title;

    @Column(name = "content", nullable = false, length = 3999)
    private String content;

    @Column(name = "views", nullable = false)
    private Long views = 0L;

    @Column(name = "is_secret",nullable = false)
    private Boolean isSecret = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "edited_at", nullable = true)
    private Timestamp editedAt;
}

package com.user.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * Comment Entity
 */
@Entity
@Table(name = "tb_comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "content", nullable = false, length = 400)
    private String content;

    @Column(name = "board_type", nullable = false, length = 10)
    private String boardType;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "author_type", nullable = false, length = 10)
    private String authorType;

    @Column(name = "author_id", nullable = false, length = 11)
    private String authorId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "edited_at", nullable = true)
    private Timestamp editedAt;
}

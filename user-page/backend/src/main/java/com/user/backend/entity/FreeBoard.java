package com.user.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * Free Board Entity
 */
@Entity
@Table(name = "tb_free_board")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreeBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "author_type", nullable = false, length = 10)
    private String authorType;

    @Column(name = "author_id", nullable = false, length = 11)
    private String authorId;

    @Column(name = "title", nullable = false, length = 99)
    private String title;

    @Column(name = "content", nullable = false, length = 3999)
    private String content;

    @Column(name = "views", nullable = false)
    private Long views = 0L;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "edited_at", nullable = true)
    private Timestamp editedAt;
}

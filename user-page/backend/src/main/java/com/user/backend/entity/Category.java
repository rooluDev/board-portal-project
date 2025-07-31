package com.user.backend.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Category Entity
 */
@Entity
@Table(name = "tb_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", nullable = false, length = 20)
    private String categoryName;

    @Column(name = "board_type", nullable = false, length = 10)
    private String boardType;

}

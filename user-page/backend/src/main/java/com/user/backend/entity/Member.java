package com.user.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * Member Entity
 */
@Entity
@Table(name = "tb_member")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @Column(name = "member_id", length = 11)
    private String memberId;

    @Column(name = "member_name", nullable = false, length = 20)
    private String memberName;

    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "edited_at", nullable = true)
    private Timestamp editedAt;
}

package com.user.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Admin Dto
 */
@Setter
@Getter
public class AdminDto {
    private String adminId;
    private String adminName;
    private String password;
    private Timestamp createdAt;
    private Timestamp editedAt;
}

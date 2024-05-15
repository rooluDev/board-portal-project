package com.user.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Member Dto
 */
@Getter
@Setter
public class MemberDto {
    @NotBlank
    @Size(min = 4, max = 11)
    private String memberId;
    @NotBlank
    @Size(min = 2, max = 11)
    private String memberName;
    private String password;
    private String passwordCheck; // join을 위한 passwordCheck
    private Timestamp createdAt;
    private Timestamp editedAt;
}

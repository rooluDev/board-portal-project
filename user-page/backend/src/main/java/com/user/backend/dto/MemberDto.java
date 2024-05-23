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
    @NotBlank(message = "ID를 입력하세요.")
    @Size(min = 4, max = 11, message = "ID를 4이상 11 이하로 입력하세요.")
    private String memberId;
    @NotBlank(message = "이름을 입력하세요.")
    @Size(min = 2, max = 4, message = "이름을 2 이상 4 이하로 입력하세요.")
    private String memberName;
    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 6, max = 12, message = "비밀번호를 6이상 12 이하로 입력하세요.")
    private String password;
    private String passwordCheck; // join을 위한 passwordCheck
    private Timestamp createdAt;
    private Timestamp editedAt;
}

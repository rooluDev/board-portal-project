package com.user.backend.service;

import com.user.backend.dto.MemberDto;

import java.util.Optional;


/**
 * Login Service
 */
public interface LoginService {

    /**
     * 로그인 진행 후 JWT 반환
     *
     * @param memberId ID
     * @param password PW
     * @return ID PW 일치하는 MemberDto
     */
    Optional<MemberDto> login(String memberId, String password);
}

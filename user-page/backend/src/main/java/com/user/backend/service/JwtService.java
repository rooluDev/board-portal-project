package com.user.backend.service;

import com.user.backend.dto.MemberDto;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Jwt Service
 */
public interface JwtService {

    /**
     * JWT에서 memberId 추출
     *
     * @param request HttpServletRequest
     * @return memberId
     */
    String getMemberIdFromToken(HttpServletRequest request);

    /**
     * JWT 생성
     *
     * @param memberDto JWT에 들어갈 정보
     * @return JWT
     */
    String createToken(MemberDto memberDto);

}

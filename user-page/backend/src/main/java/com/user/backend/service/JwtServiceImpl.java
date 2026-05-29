package com.user.backend.service;

import com.user.backend.common.exception.custom.NotLoggedInException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.dto.MemberDto;
import com.user.backend.jwt.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final JwtProvider jwtProvider;

    /**
     * JWT에서 memberId 추출
     *
     * @param request HttpServletRequest
     * @return memberId (만료된 토큰이면 null 반환)
     */
    @Override
    public String getMemberIdFromToken(HttpServletRequest request) {
        try {
            String accessToken = jwtProvider.getHeaderFromToken(request);
            return jwtProvider.getMemberIdFromJwt(accessToken);
        } catch (MalformedJwtException | IllegalArgumentException e) {
            throw new NotLoggedInException(ErrorCode.NOT_LOGGED_IN);
        } catch (ExpiredJwtException e){
            return null;
        }
    }

    /**
     * JWT 생성
     *
     * @param memberDto JWT에 들어갈 회원 정보
     * @return 생성된 JWT 문자열
     */
    @Override
    public String createToken(MemberDto memberDto) {
        return jwtProvider.createAccessToken(memberDto.getMemberId(), memberDto.getMemberName());
    }

}

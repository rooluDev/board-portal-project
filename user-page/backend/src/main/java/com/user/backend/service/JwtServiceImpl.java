package com.user.backend.service;

import com.user.backend.common.exception.custom.NotLoggedInException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.dto.MemberDto;
import com.user.backend.jwt.JwtProvider;
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

    @Override
    public String getMemberIdFromToken(HttpServletRequest request) {
        try {
            String accessToken = jwtProvider.getHeaderFromToken(request);
            return jwtProvider.getMemberIdFromJwt(accessToken);
        } catch (MalformedJwtException e) {
            throw new NotLoggedInException(ErrorCode.NOT_LOGGED_IN);
        } catch (IllegalArgumentException e) {
            throw new NotLoggedInException(ErrorCode.NOT_LOGGED_IN);
        }
    }

    @Override
    public String createToken(MemberDto memberDto) {
        return jwtProvider.createAccessToken(memberDto.getMemberId(), memberDto.getMemberName());
    }

    @Override
    public String getMemberNameFromToken(HttpServletRequest request) {
        try {
            String accessToken = jwtProvider.getHeaderFromToken(request);
            return jwtProvider.getMemberNameFromJwt(accessToken);
        } catch (MalformedJwtException e) {
            throw new NotLoggedInException(ErrorCode.NOT_LOGGED_IN);
        } catch (IllegalArgumentException e) {
            throw new NotLoggedInException(ErrorCode.NOT_LOGGED_IN);
        }
    }
}

package com.user.backend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Jwt Provider
 */
@Component
@Slf4j
public class JwtProvider implements InitializingBean {

    @Value("#{jwt['secret']}")
    private String secretKey;

    @Value("#{jwt['headerKey']}")
    private String headerKey;

    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT 생성
     *
     * @param memberId   memberId
     * @param memberName memberName
     * @return JWT
     */
    public String createAccessToken(String memberId, String memberName) {
        // Claim Subject 설정
        Claims claims = Jwts.claims().setSubject(memberId);
        // 이름 추가
        claims.put("memberName", memberName);

        // expire 설정
        Date now = new Date();
        Date accessValidity = new Date(now.getTime() + 999999999);

        // 빌드 후 return
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(accessValidity) // 만료시간 설정
                .compact();
    }

    /**
     * 헤더에서 토큰 꺼내기
     *
     * @param request HttpServletRequest
     * @return JWT
     */
    public String getHeaderFromToken(HttpServletRequest request) {
        // 헤더에서 accessToken 가져오기
        String accessToken = request.getHeader(headerKey);

        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }
        return accessToken;
    }

    /**
     * JWT에서 memberId 추출
     *
     * @param accessToken JWT
     * @return memberId
     */
    public String getMemberIdFromJwt(String accessToken) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        return claims.getSubject();
    }
}

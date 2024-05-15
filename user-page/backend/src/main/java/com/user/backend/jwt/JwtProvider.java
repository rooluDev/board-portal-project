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
        Claims claims = Jwts.claims().setSubject(memberId);
        claims.put("memberName", memberName);

        Date now = new Date();
        Date accessValidity = new Date(now.getTime() + 999999999);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(accessValidity) // 만료시간 설정
                .compact();

        return accessToken;
    }

    /**
     * 헤더에서 토큰 꺼내기
     *
     * @param request HttpServletRequest
     * @return JWT
     */
    public String getHeaderFromToken(HttpServletRequest request) {

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

    /**
     * JWT에서 memberName 추출
     *
     * @param accessToken JWT
     * @return memberNAme
     */
    public String getMemberNameFromJwt(String accessToken) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        return claims.get("memberName").toString();
    }
}

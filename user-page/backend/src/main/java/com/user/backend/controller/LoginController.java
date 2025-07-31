package com.user.backend.controller;

import com.user.backend.common.exception.custom.LoginFailException;
import com.user.backend.common.exception.custom.MemberNotFoundException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.dto.MemberDto;
import com.user.backend.service.JwtService;
import com.user.backend.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Login Controller
 */
@RequestMapping("/api")
@Slf4j
@RestController
public class LoginController {

    private final LoginService loginService;
    private final JwtService jwtService;

    public LoginController(@Qualifier("loginJpa") LoginService loginService,
                           JwtService jwtService) {
        this.loginService = loginService;
        this.jwtService = jwtService;
    }

    /**
     * 로그인
     *
     * @param memberDto ID, PW
     * @return JWT
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberDto memberDto) {

        MemberDto member = loginService.login(memberDto.getMemberId(), memberDto.getPassword())
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        String accessToken = jwtService.createToken(member);

        return ResponseEntity.ok(accessToken);

    }
}




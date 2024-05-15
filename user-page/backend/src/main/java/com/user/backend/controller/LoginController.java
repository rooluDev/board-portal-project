package com.user.backend.controller;

import com.user.backend.common.exception.custom.LoginFailException;
import com.user.backend.common.exception.custom.MemberNotFoundException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.dto.MemberDto;
import com.user.backend.service.JwtService;
import com.user.backend.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Login Controller
 */
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@RestController
public class LoginController {

    private final LoginService loginService;
    private final JwtService jwtService;

    /**
     * 로그인
     *
     * @param memberDto ID, PW
     * @return JWT
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberDto memberDto) {
        String accessToken = null;
        try {
            MemberDto member = loginService.login(memberDto.getMemberId(), memberDto.getPassword());
            accessToken = jwtService.createToken(member);
        } catch (MemberNotFoundException e) {
            new LoginFailException(ErrorCode.LOGIN_FAIL);
        }
        return ResponseEntity.ok(accessToken);
    }
}




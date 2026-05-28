package com.user.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.backend.dto.MemberDto;
import com.user.backend.service.JwtService;
import com.user.backend.service.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean(name = "loginJpa")
    private LoginService loginService;

    @MockBean
    private JwtService jwtService;

    @Test
    @DisplayName("로그인 성공 - JWT 토큰 반환")
    void login_withValidCredentials_returnsToken() throws Exception {
        // given
        MemberDto requestDto = new MemberDto();
        requestDto.setMemberId("user1234");
        requestDto.setMemberName("홍길동");
        requestDto.setPassword("pass1234");

        MemberDto foundMember = new MemberDto();
        foundMember.setMemberId("user1234");
        foundMember.setMemberName("홍길동");
        foundMember.setPassword("pass1234");

        when(loginService.login("user1234", "pass1234")).thenReturn(Optional.of(foundMember));
        when(jwtService.createToken(foundMember)).thenReturn("jwt.token.here");

        // when & then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt.token.here"));
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 ID/PW는 404 반환")
    void login_withInvalidCredentials_returns404() throws Exception {
        // given
        MemberDto requestDto = new MemberDto();
        requestDto.setMemberId("user1234");
        requestDto.setMemberName("홍길동");
        requestDto.setPassword("wrongpas");

        when(loginService.login("user1234", "wrongpas")).thenReturn(Optional.empty());

        // when & then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }
}

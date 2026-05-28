package com.admin.backend.controller;

import com.admin.backend.dto.AdminDto;
import com.admin.backend.service.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    @DisplayName("로그인 페이지 GET - 200 반환")
    void getLoginPage_returnsOk() throws Exception {
        // /login 은 인터셉터에서 제외 - 세션 없이 접근 가능
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @DisplayName("로그인 성공 - /board/notice로 리다이렉트")
    void loginProc_withValidCredentials_redirectsToNotice() throws Exception {
        // given
        AdminDto admin = new AdminDto();
        admin.setAdminId("admin");
        admin.setAdminName("관리자");
        when(adminService.findAdmin("admin", "password")).thenReturn(Optional.of(admin));

        // when & then
        mockMvc.perform(post("/login")
                        .param("adminId", "admin")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/notice"));
    }

    @Test
    @DisplayName("로그인 실패 - /login으로 리다이렉트")
    void loginProc_withInvalidCredentials_redirectsToLogin() throws Exception {
        // given
        when(adminService.findAdmin("admin", "wrong")).thenReturn(Optional.empty());

        // when & then
        mockMvc.perform(post("/login")
                        .param("adminId", "admin")
                        .param("password", "wrong"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("로그아웃 - /login으로 리다이렉트")
    void logoutProc_withSession_redirectsToLogin() throws Exception {
        // given
        AdminDto admin = new AdminDto();
        admin.setAdminId("admin");
        admin.setAdminName("관리자");

        // when & then (세션 속성을 설정하여 인터셉터 통과)
        mockMvc.perform(get("/logout")
                        .sessionAttr(LoginController.ADMIN_SESSION_ID, admin))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}

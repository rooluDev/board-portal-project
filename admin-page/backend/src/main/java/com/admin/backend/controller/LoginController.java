package com.admin.backend.controller;

import com.admin.backend.common.exception.LoginFailException;
import com.admin.backend.dto.AdminDto;
import com.admin.backend.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * Login Controller
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    public static final String ADMIN_SESSION_ID = "ADMIN_SESSION_ID";
    private static final int SESSION_TIME = 60 * 30;
    private final AdminService adminService;

    /**
     * 로그인 페이지
     *
     * @return login.html
     */
    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    /**
     * 로그인 진행
     *
     * @param adminDto           login form data ( ID, PW )
     * @param httpServletRequest HttpServletRequest
     * @return redirect:/board/notice
     */
    @PostMapping("/login")
    public String loginProc(@ModelAttribute AdminDto adminDto, HttpServletRequest httpServletRequest) {

        // 로그인 검증
        AdminDto admin = adminService.findAdmin(adminDto.getAdminId(), adminDto.getPassword())
                .orElseThrow(() -> new LoginFailException("회원 정보가 일치하지 않습니다."));

        // 세션 생성
        HttpSession session = httpServletRequest.getSession(true);
        log.info("Admin Id : " + admin.getAdminId() + " Login");
        session.setAttribute(ADMIN_SESSION_ID, admin);

        // 시간 설정
        session.setMaxInactiveInterval(SESSION_TIME);

        return "redirect:/board/notice";
    }

    /**
     * 로그아웃 진행
     *
     * @param request  HttpServletRequest
     * @param adminDto 세션에 들어이있는 정보
     * @return redirect:/login
     */
    @GetMapping("/logout")
    public String logoutProc(HttpServletRequest request,
                             @SessionAttribute(name = ADMIN_SESSION_ID) AdminDto adminDto) {

        // 세션 가져오기
        HttpSession session = request.getSession(false);

        if (session != null) {
            // 세션 파기
            session.invalidate();
            log.info("Admin Id : " + adminDto.getAdminId() + " Logout");
        }

        return "redirect:/login";
    }
}

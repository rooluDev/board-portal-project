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

/**
 * Login Controller
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    public static final String ADMIN_SESSION_ID = "ADMIN_SESSION_ID";
    private final AdminService adminService;

    /**
     * 로그인 페이지
     * @return
     */
    @GetMapping("/admin/login")
    public String getLogin(){
        return "login";
    }

    /**
     * 로그인 프로세서
     * @param adminDto
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/admin/login")
    public String loginProc(@ModelAttribute AdminDto adminDto, HttpServletRequest httpServletRequest){
        // 로그인 검증
        // method가 객체를 매개변수로 받아 명확하지 않음. 파라매터로 ID, PW 보내는게 좀 더 명확해 보임
        AdminDto admin = adminService.findAdmin(adminDto.getAdminId(), adminDto.getPassword())
                .orElseThrow(() -> new LoginFailException("회원 정보가 일치하지 않습니다."));
        // 세션 생성
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(ADMIN_SESSION_ID,admin);
        session.setMaxInactiveInterval(60 * 60);

        return "redirect:/admin/board/notice";
    }


}

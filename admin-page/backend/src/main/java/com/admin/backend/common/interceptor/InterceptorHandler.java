package com.admin.backend.common.interceptor;

import com.admin.backend.controller.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * InterceptorHandler
 */
@Component
public class InterceptorHandler implements HandlerInterceptor {

    /**
     * 요청 전 처리 - 세션 유효성 검증
     * 세션이 없으면 로그인 페이지로 리다이렉트
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param handler  처리 핸들러
     * @return 세션이 있으면 true, 없으면 false
     * @throws Exception 예외 발생 시
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        // 세션 확인
        Object user = session.getAttribute(LoginController.ADMIN_SESSION_ID);

        // 세션 없을 시 로그인 페이지로
        if(user == null){
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}

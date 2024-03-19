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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        // 세션 확인
        Object user = session.getAttribute(LoginController.ADMIN_SESSION_ID);

        // 세션 없을 시 로그인 페이지로
        if(user == null){
            response.sendRedirect("/admin/login");
            return false;
        }
        return true;
    }
}

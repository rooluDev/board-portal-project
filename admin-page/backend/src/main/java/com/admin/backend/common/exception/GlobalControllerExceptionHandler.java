package com.admin.backend.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(LoginFailException.class)
    public String handleLoginFailException(LoginFailException loginFailException, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage",loginFailException.getMessage());
        return "redirect:/admin/login";
    }
}

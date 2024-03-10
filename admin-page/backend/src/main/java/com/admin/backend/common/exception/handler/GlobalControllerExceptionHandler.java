package com.admin.backend.common.exception.handler;

import com.admin.backend.common.exception.LoginFailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Global Controller Exception Handler
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * LoginFailException Handler
     * @param loginFailException
     * @param redirectAttributes
     * @return
     */
    @ExceptionHandler(LoginFailException.class)
    public String handleLoginFailException(LoginFailException loginFailException, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage",loginFailException.getMessage());
        return "redirect:/admin/login";
    }
}

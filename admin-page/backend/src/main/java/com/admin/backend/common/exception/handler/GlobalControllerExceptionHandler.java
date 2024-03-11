package com.admin.backend.common.exception.handler;

import com.admin.backend.common.exception.FixedBoardFullException;
import com.admin.backend.common.exception.IllegalBoardDataException;
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
     *
     * @param loginFailException
     * @param redirectAttributes
     * @return
     */
    @ExceptionHandler(LoginFailException.class)
    public String handleLoginFailException(LoginFailException loginFailException, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", loginFailException.getMessage());

        return "redirect:/admin/login";
    }

    /**
     * FixedBoardFullException Handler
     *
     * @param fixedBoardFullException
     * @param redirectAttributes
     * @return
     */
    @ExceptionHandler(FixedBoardFullException.class)
    public String handleFixedBoardFullException(FixedBoardFullException fixedBoardFullException, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", fixedBoardFullException.getMessage());
        // TODO : 검색조건 잃어버림
        return "redirect:/admin/board/notice/write";
    }

    /**
     * IllegalBoardDataException Handler
     *
     * @param illegalBoardDataException
     * @param redirectAttributes
     * @return
     */
    @ExceptionHandler(IllegalBoardDataException.class)
    public String handleIllegalBoardDataException(IllegalBoardDataException illegalBoardDataException, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", illegalBoardDataException.getMessage());

        return "redirect:/admin/board/notice/write";
    }
}

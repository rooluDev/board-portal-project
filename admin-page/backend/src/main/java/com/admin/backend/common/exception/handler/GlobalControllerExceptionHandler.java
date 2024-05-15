package com.admin.backend.common.exception.handler;

import com.admin.backend.common.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Global Controller Exception Handler
 */
@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    /**
     * LoginFailException Handler
     *
     * @param loginFailException LoginFailException
     * @param redirectAttributes RedirectAttributes
     * @param request            HttpServletRequest
     * @return redirect:/login
     */
    @ExceptionHandler(LoginFailException.class)
    public String handleLoginFailException(LoginFailException loginFailException,
                                           RedirectAttributes redirectAttributes,
                                           HttpServletRequest request) {

        log.error("Exception: LoginFailException / URI: " + request.getRequestURI());

        redirectAttributes.addFlashAttribute("errorMessage", loginFailException.getMessage());

        return "redirect:/login";
    }

    /**
     * BoardNotFoundException Handler
     *
     * @param request HttpServletRequest
     * @return redirect:/error
     */
    @ExceptionHandler(BoardNotFoundException.class)
    public String handleBoardNotFoundException(HttpServletRequest request) {

        log.error("Exception: BoardNotFoundException / URI: " + request.getRequestURI());

        return "redirect:/error";
    }

    /**
     * 갤러리 게시판에 파일을 등록하지 않았을 때 발생하는 MissingServletRequestPartException Handler
     *
     * @param request            HttpServletRequest
     * @param redirectAttributes RedirectAttributes
     * @return redirect:/board/gallery/write?" + request.getQueryString()
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    public String handleMissingServletRequestPartException(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", "파일을 1개 이상 등록하세요");

        return "redirect:/board/gallery/write?" + request.getQueryString();
    }

    /**
     * CommentNotFoundException Handler
     *
     * @param commentNotFoundException
     * @param redirectAttributes
     * @return redirect:/error
     */
    @ExceptionHandler(CommentNotFoundException.class)
    public String handleCommentNotFoundException(CommentNotFoundException commentNotFoundException,
                                                 RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", commentNotFoundException.getMessage());

        return "redirect:/error";
    }


    /**
     * StorageFailException Handler
     *
     * @return redirect:/error
     */
    @ExceptionHandler(StorageFailException.class)
    public String handleStorageFailException() {

        return "redirect:/error";
    }

    /**
     * MaxUploadSizeExceededException Handler
     *
     * @return redirect:/error
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException() {
        return "redirect:/error";
    }
}

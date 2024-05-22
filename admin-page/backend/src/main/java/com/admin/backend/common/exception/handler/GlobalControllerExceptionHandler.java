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
     * @return redirect:/login
     */
    @ExceptionHandler(LoginFailException.class)
    public String handleLoginFailException(LoginFailException loginFailException,
                                           RedirectAttributes redirectAttributes) {

        log.info("LoginFail Message: {}", loginFailException.getMessage());

        redirectAttributes.addFlashAttribute("errorMessage", loginFailException.getMessage());

        return "redirect:/login";
    }

    /**
     * BoardNotFoundException Handler
     *
     * @return redirect:/error
     */
    @ExceptionHandler(BoardNotFoundException.class)
    public String handleBoardNotFoundException(BoardNotFoundException e) {

        log.info("BoardNotFoundException: {}", e.getMessage());

        return "redirect:/error";
    }

    /**
     * CommentNotFoundException Handler
     *
     * @param e CommentNotFoundException
     * @return redirect:/error
     */
    @ExceptionHandler(CommentNotFoundException.class)
    public String handleCommentNotFoundException(CommentNotFoundException e) {
        log.error("Error message: {}", e.getMessage());
        log.error("Error on: ", e);

        return "redirect:/error";
    }


    /**
     * StorageFailException Handler
     *
     * @return redirect:/error
     */
    @ExceptionHandler(StorageFailException.class)
    public String handleStorageFailException(StorageFailException e) {

        log.error("Error message: {}", e.getMessage());
        log.error("Error on: ", e);

        return "redirect:/error";
    }

    /**
     * MaxUploadSizeExceededException Handler
     *
     * @return redirect:/error
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("Error message: {}", e.getMessage());
        log.error("Error on: ", e);

        return "redirect:/error";
    }

    /**
     * Exception Handler
     *
     * @param e Exception
     * @return redirect:/error
     */
    @ExceptionHandler(Exception.class)
    public String handleDefaultHandler(Exception e) {
        log.error("Error message: {}", e.getMessage());
        log.error("Error on: ", e);

        return "redirect:/error";
    }
}

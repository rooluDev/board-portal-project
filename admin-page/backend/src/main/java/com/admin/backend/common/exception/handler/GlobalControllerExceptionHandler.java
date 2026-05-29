package com.admin.backend.common.exception.handler;

import com.admin.backend.common.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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
     * NoResourceFoundException Handler (favicon.ico 등 정적 리소스 없음 - 404 응답)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFoundException(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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

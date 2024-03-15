package com.admin.backend.common.exception.handler;

import com.admin.backend.common.exception.*;
import com.admin.backend.common.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public String handleFixedBoardFullException(FixedBoardFullException fixedBoardFullException, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        // TODO : 검색조건 잃어버림
        String uri = request.getRequestURI();

        return getDirectionByUriCase(uri, redirectAttributes, fixedBoardFullException);
    }

    /**
     * IllegalBoardDataException Handler
     *
     * @param illegalBoardDataException
     * @param redirectAttributes
     * @return
     */
    @ExceptionHandler(IllegalBoardDataException.class)
    public String handleIllegalBoardDataException(IllegalBoardDataException illegalBoardDataException, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String uri = request.getRequestURI();

        return getDirectionByUriCase(uri, redirectAttributes, illegalBoardDataException);

    }

    /**
     * BoardNotFoundException Handler
     *
     * @param boardNotFoundException
     * @param redirectAttributes
     * @return
     */
    @ExceptionHandler(BoardNotFoundException.class)
    public String handleBoardNotFoundException(BoardNotFoundException boardNotFoundException, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", boardNotFoundException.getMessage());

        return "redirect:/admin/board/notice";

    }

    /**
     * CommentNotFoundException Handler
     *
     * @param commentNotFoundException
     * @param redirectAttributes
     * @return
     */
    @ExceptionHandler(CommentNotFoundException.class)
    public String handleCommentNotFoundException(CommentNotFoundException commentNotFoundException, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", commentNotFoundException.getMessage());
        return "redirect:/admin/board/free";
    }

    /**
     * IllegalAnswerDataException Handler
     *
     * @param illegalAnswerDataException
     * @param redirectAttributes
     * @param request
     * @return
     */
    @ExceptionHandler(IllegalAnswerDataException.class)
    public String handleIllegalAnswerDataException(IllegalAnswerDataException illegalAnswerDataException, RedirectAttributes redirectAttributes, HttpServletRequest request){
        String uri = request.getRequestURI();
        return getDirectionByUriCase(uri, redirectAttributes, illegalAnswerDataException);
    }

    /**
     * IllegalFileDataException Handler
     *
     * @param illegalFileDataException
     * @param redirectAttributes
     * @param request
     * @return
     */
    @ExceptionHandler(IllegalFileDataException.class)
    public String handleIllegalFileDataException(IllegalFileDataException illegalFileDataException, RedirectAttributes redirectAttributes, HttpServletRequest request){
        String uri = request.getRequestURI();
        return getDirectionByUriCase(uri, redirectAttributes, illegalFileDataException);
    }

    private String getDirectionByUriCase(String uri, RedirectAttributes redirectAttributes, RuntimeException runtimeException) {
        if (uri.contains("/notice/write")) {
            redirectAttributes.addFlashAttribute("errorMessage", runtimeException.getMessage());
            return "redirect:/admin/board/notice/write";
        } else if (uri.contains("/notice/modify")) {

            String boardId = StringUtils.extractNumberFromUri(uri).get(0);
            redirectAttributes.addFlashAttribute("errorMessage", runtimeException.getMessage());

            return "redirect:/admin/board/notice/" + boardId;
        } else if (uri.contains("/free/write")) {

            redirectAttributes.addFlashAttribute("errorMessage", runtimeException.getMessage());

            return "redirect:/admin/board/free/write";
        } else if(uri.contains("/inquiry")){
            String boardId = StringUtils.extractNumberFromUri(uri).get(0);
            redirectAttributes.addFlashAttribute("errorMessage", runtimeException.getMessage());

            return "redirect:/admin/board/inquiry/" + boardId;
        }
        return "redirect:/error";
    }
}

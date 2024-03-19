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
     * @param loginFailException LoginFailException
     * @param redirectAttributes RedirectAttributes
     * @return redirect:/admin/login
     */
    @ExceptionHandler(LoginFailException.class)
    public String handleLoginFailException(LoginFailException loginFailException,
                                           RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", loginFailException.getMessage());

        return "redirect:/admin/login";
    }

    /**
     * FixedBoardFullException Handler
     *
     * @param fixedBoardFullException FixedBoardFullException
     * @param redirectAttributes      RedirectAttributes
     * @param request                 HttpServletRequest
     * @return redirect:/admin/board/notice or /write
     */
    @ExceptionHandler(FixedBoardFullException.class)
    public String handleFixedBoardFullException(FixedBoardFullException fixedBoardFullException,
                                                RedirectAttributes redirectAttributes,
                                                HttpServletRequest request) {

        String uri = request.getRequestURI();

        return getDirectionByUriCase(uri, redirectAttributes, fixedBoardFullException);
    }

    /**
     * IllegalBoardDataException Handler
     *
     * @param illegalBoardDataException IllegalBoardDataException
     * @param redirectAttributes        RedirectAttributes
     * @param request                   HttpServletRequest
     * @return redirect getDirectionByUriCase()
     */
    @ExceptionHandler(IllegalBoardDataException.class)
    public String handleIllegalBoardDataException(IllegalBoardDataException illegalBoardDataException,
                                                  RedirectAttributes redirectAttributes,
                                                  HttpServletRequest request) {

        String uri = request.getRequestURI();

        return getDirectionByUriCase(uri, redirectAttributes, illegalBoardDataException);

    }

    /**
     * BoardNotFoundException Handler
     *
     * @param boardNotFoundException BoardNotFoundException
     * @param redirectAttributes     RedirectAttributes
     * @param request                HttpServletRequest
     * @return redirect getDirectionByUriCase()
     */
    @ExceptionHandler(BoardNotFoundException.class)
    public String handleBoardNotFoundException(BoardNotFoundException boardNotFoundException,
                                               RedirectAttributes redirectAttributes,
                                               HttpServletRequest request) {

        redirectAttributes.addFlashAttribute("errorMessage", boardNotFoundException.getMessage());

        String uri = request.getRequestURI();

        if (uri.contains("notice")) {
            return "redirect:/admin/board/notice";
        } else if (uri.contains("inquiry")) {
            return "redirect:/admin/board/inquiry";
        } else if (uri.contains("free")) {
            return "redirect:/admin/board/free";
        } else if (uri.contains("gallery")) {
            return "redirect:/admin/board/gallery";
        }
        return "redirect:/error";

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
     * IllegalAnswerDataException Handler
     *
     * @param illegalAnswerDataException
     * @param redirectAttributes
     * @param request
     * @return redirect getDirectionByUriCase()
     */
    @ExceptionHandler(IllegalAnswerDataException.class)
    public String handleIllegalAnswerDataException(IllegalAnswerDataException illegalAnswerDataException,
                                                   RedirectAttributes redirectAttributes,
                                                   HttpServletRequest request) {

        String uri = request.getRequestURI();
        return getDirectionByUriCase(uri, redirectAttributes, illegalAnswerDataException);
    }

    /**
     * IllegalFileDataException Handler
     *
     * @param illegalFileDataException IllegalFileDataException
     * @param redirectAttributes       RedirectAttributes
     * @param request                  HttpServletRequest
     * @return redirect getDirectionByUriCase()
     */
    @ExceptionHandler(IllegalFileDataException.class)
    public String handleIllegalFileDataException(IllegalFileDataException illegalFileDataException,
                                                 RedirectAttributes redirectAttributes,
                                                 HttpServletRequest request) {

        String uri = request.getRequestURI();
        return getDirectionByUriCase(uri, redirectAttributes, illegalFileDataException);
    }

    /**
     * IllegalSearchConditionDataException Handler
     *
     * @param illegalSearchConditionDataException
     * @param redirectAttributes
     * @param request
     * @return
     */
    @ExceptionHandler(IllegalSearchConditionDataException.class)
    public String handleIllegalSearchConditionDataException(IllegalSearchConditionDataException illegalSearchConditionDataException,
                                                            RedirectAttributes redirectAttributes,
                                                            HttpServletRequest request) {

        redirectAttributes.addFlashAttribute("errorMessage", illegalSearchConditionDataException.getMessage());

        String uri = request.getRequestURI();

        return "redirect:" + uri;
    }

    private String getDirectionByUriCase(String uri, RedirectAttributes redirectAttributes, RuntimeException runtimeException) {

        if (uri.contains("/notice/write")) {

            redirectAttributes.addFlashAttribute("errorMessage", runtimeException.getMessage());

            return "redirect:/admin/board/notice/write";

        } else if (uri.contains("/notice/modify")) {

            String boardId = StringUtils.extractNumberFromUri(uri).get(0);
            redirectAttributes.addFlashAttribute("errorMessage", runtimeException.getMessage());

            return "redirect:/admin/board/notice/" + boardId;

        } else if (uri.contains("/inquiry")) {

            String boardId = StringUtils.extractNumberFromUri(uri).get(0);
            redirectAttributes.addFlashAttribute("errorMessage", runtimeException.getMessage());

            return "redirect:/admin/board/inquiry/" + boardId;

        } else if (uri.contains("/answer")) {

            redirectAttributes.addFlashAttribute("errorMessage", runtimeException.getMessage());
            String boardId = StringUtils.extractNumberFromUri(uri).get(0);

            return "redirect:/admin/board/inquiry/" + boardId;

        } else if (uri.contains("/free/write")) {

            redirectAttributes.addFlashAttribute("errorMessage", runtimeException.getMessage());

            return "redirect:/admin/board/free/write";

        } else if (uri.contains("/free/modify")) {

            redirectAttributes.addFlashAttribute("errorMessage", runtimeException.getMessage());
            String boardId = StringUtils.extractNumberFromUri(uri).get(0);

            return "redirect:/admin/board/free/" + boardId;

        } else if (uri.contains("/gallery/write")) {

            redirectAttributes.addFlashAttribute("errorMessage", runtimeException.getMessage());

            return "redirect:/admin/board/free/write";

        } else if (uri.contains("/gallery/modify")) {

            redirectAttributes.addFlashAttribute("errorMessage", runtimeException.getMessage());
            String boardId = StringUtils.extractNumberFromUri(uri).get(0);

            return "redirect:/admin/board/gallery/" + boardId;

        }

        return "redirect:/error";
    }
}

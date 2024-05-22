package com.admin.backend.controller;

import com.admin.backend.common.utils.BindingResultUtils;
import com.admin.backend.common.utils.StringUtils;
import com.admin.backend.dto.AdminDto;
import com.admin.backend.dto.AnswerDto;
import com.admin.backend.dto.SearchConditionDto;
import com.admin.backend.service.AnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Answer Controller
 */
@Controller
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    /**
     * 답변 등록
     *
     * @param adminDto           저장된 세션 정보 ( table column에 저장될 데이터 )
     * @param answerDto          답변 내용
     * @param searchConditionDto 등록 후 검색조건을 유지하기 위한 쿼리스트링
     * @param redirectAttributes 등록 완료 alert를 위한 redirectAttributes
     * @return redirect:/board/inquiry
     */
    @PostMapping("/answer")
    public String addAnswer(@SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                            @Valid @ModelAttribute AnswerDto answerDto,
                            BindingResult bindingResult,
                            @ModelAttribute SearchConditionDto searchConditionDto,
                            RedirectAttributes redirectAttributes) {
        // 유효성 검증
        if (bindingResult.hasErrors()) {
            String errorMessage = BindingResultUtils.getErrorMessage(bindingResult, new String[]{"content"});
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);

            return "redirect:/board/notice" + answerDto.getBoardId() + StringUtils.searchConditionToQueryStringWithOutCategory(searchConditionDto);
        }

        answerDto.setAuthorId(adminDto.getAdminId());

        // 답변이 이미 존재 할시 수정 그 외 추가
        if (answerService.getAnswerByBoardId(answerDto.getBoardId()).isPresent()) {
            answerService.modifyAnswer(answerDto);
        } else {
            answerService.addAnswer(answerDto);
        }

        // 등록 완료 응답 값 설정
        redirectAttributes.addFlashAttribute("write", 1);

        return "redirect:/board/inquiry" + StringUtils.searchConditionToQueryStringWithOutCategory(searchConditionDto);
    }
}

package com.admin.backend.controller;

import com.admin.backend.common.validator.AnswerValidator;
import com.admin.backend.dto.AdminDto;
import com.admin.backend.dto.AnswerDto;
import com.admin.backend.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Answer Controller
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    /**
     * 답변 등록
     *
     * @param boardId  PathVariable
     * @param adminDto 저장된 세션 정보 ( table column에 저장될 데이터 )
     * @param content  답변 내용
     * @return redirect:/admin/board/inquiry
     */
    @PostMapping("/answer/{boardId}")
    public String addAnswer(@PathVariable(name = "boardId") Long boardId,
                            @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                            @RequestParam(name = "content") String content) {

        // 유효성 검증
        AnswerValidator.validateAnswer(content);

        AnswerDto answerDto = AnswerDto.builder()
                .boardId(boardId)
                .authorId(adminDto.getAdminId())
                .content(content)
                .build();

        // 답변이 이미 존재 할시 수정 그 외 추가
        if (answerService.getAnswerByBoardId(boardId).isPresent()) {
            answerService.modifyAnswer(answerDto);
        } else {
            answerService.addAnswer(answerDto);
        }

        return "redirect:/admin/board/inquiry";
    }
}

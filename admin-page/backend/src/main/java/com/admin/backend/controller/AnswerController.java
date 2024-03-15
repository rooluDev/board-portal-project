package com.admin.backend.controller;

import com.admin.backend.common.validator.AnswerValidator;
import com.admin.backend.dto.AdminDto;
import com.admin.backend.dto.AnswerDto;
import com.admin.backend.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    /**
     * 답변 등록
     *
     * @param boardId
     * @param content
     * @param adminDto
     * @return
     */
    @PostMapping("/answer/{boardId}")
    public String addAnswer(@PathVariable(name = "boardId") Long boardId,
                            @RequestParam(name = "content") String content,
                            @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto) {

        // 유효성 검증
        AnswerValidator.validateAnswer(content);

        AnswerDto answerDto = AnswerDto.builder()
                .boardId(boardId)
                .authorId(adminDto.getAdminId())
                .content(content)
                .build();

        // 답변 추가
        if(answerService.getAnswerByBoardId(boardId).isPresent()){
            answerService.modifyAnswer(answerDto);
            return "redirect:/admin/board/inquiry";
        }
        answerService.addAnswer(answerDto);

        return "redirect:/admin/board/inquiry";
    }
}

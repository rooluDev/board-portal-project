package com.user.backend.service;

import com.user.backend.dto.AnswerDto;

import java.util.Optional;


/**
 * Answer Service Interface
 */
public interface AnswerService {

    /**
     * boardId와 일치하는 문의 게시물에 있는 답변 가져오기
     *
     * @param boardId boardId
     * @return 문의 게시물에 등록된 답변
     */
    Optional<AnswerDto> getAnswerByBoardId(Long boardId);
}

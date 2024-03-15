package com.admin.backend.service;

import com.admin.backend.dto.AnswerDto;


/**
 * Answer Service Interface
 */
public interface AnswerService {

    /**
     * boardId에 등록된 answer 가져오기
     *
     * @param boardId
     * @return
     */
    AnswerDto getAnswerByBoardId(Long boardId);

    /**
     * 답변 추가
     *
     * @param answerDto
     */
    void addAnswer(AnswerDto answerDto);

    /**
     * 답변 삭제
     *
     * @param boardId
     */
    void deleteAnswer(Long boardId);

    /**
     * 답변 수정
     *
     * @param answerDto
     */
    void modifyAnswer(AnswerDto answerDto);
}

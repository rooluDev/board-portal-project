package com.admin.backend.service;

import com.admin.backend.dto.AnswerDto;

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

    /**
     * 문의 게시물에 답변 추가
     *
     * @param answerDto ( boardId, authorId, content )
     */
    void addAnswer(AnswerDto answerDto);

    /**
     * 문의 게시물에 등록된 답변 삭제
     *
     * @param boardId 문의 게시물의 pk
     */
    void deleteAnswer(Long boardId);

    /**
     * 문의 게시물에 등록된 답변 수정
     *
     * @param answerDto ( authorId, content , boardId )
     */
    void modifyAnswer(AnswerDto answerDto);
}

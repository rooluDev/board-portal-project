package com.admin.backend.service;

import com.admin.backend.dto.AnswerDto;
import com.admin.backend.mapper.AnswerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Answer Service 구현체
 */
@Service
@Primary
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService{

    private final AnswerMapper answerMapper;

    /**
     * boardId와 일치하는 문의 게시물에 있는 답변 가져오기
     *
     * @param boardId 문의 게시물의 pk
     * @return 문의 게시물에 등록된 답변
     */
    @Override
    public Optional<AnswerDto> getAnswerByBoardId(Long boardId) {
        return answerMapper.selectByBoardId(boardId);
    }

    /**
     * 문의 게시물에 답변 추가
     *
     * @param answerDto 등록할 답변 데이터 (boardId, authorId, content)
     */
    @Override
    public void addAnswer(AnswerDto answerDto) {
        answerMapper.insertAnswer(answerDto);
    }

    /**
     * 문의 게시물에 등록된 답변 삭제
     *
     * @param boardId 문의 게시물의 pk
     */
    @Override
    public void deleteAnswer(Long boardId) {
        answerMapper.deleteByBoardId(boardId);
    }

    /**
     * 문의 게시물에 등록된 답변 수정
     *
     * @param answerDto 수정할 답변 데이터 (authorId, content, boardId)
     */
    @Override
    public void modifyAnswer(AnswerDto answerDto) {
        answerMapper.updateAnswer(answerDto);
    }
}

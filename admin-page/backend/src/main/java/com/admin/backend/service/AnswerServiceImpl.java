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

    @Override
    public Optional<AnswerDto> getAnswerByBoardId(Long boardId) {
        return answerMapper.selectByBoardId(boardId);
    }

    @Override
    public void addAnswer(AnswerDto answerDto) {
        answerMapper.insertAnswer(answerDto);
    }

    @Override
    public void deleteAnswer(Long boardId) {
        answerMapper.deleteByBoardId(boardId);
    }
}

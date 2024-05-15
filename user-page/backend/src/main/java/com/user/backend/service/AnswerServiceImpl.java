package com.user.backend.service;

import com.user.backend.dto.AnswerDto;
import com.user.backend.mapper.AnswerMapper;
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
public class AnswerServiceImpl implements AnswerService {

    private final AnswerMapper answerMapper;

    @Override
    public Optional<AnswerDto> getAnswerByBoardId(Long boardId) {
        return answerMapper.selectByBoardId(boardId);
    }

}

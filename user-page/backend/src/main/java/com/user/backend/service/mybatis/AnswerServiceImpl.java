package com.user.backend.service.mybatis;

import com.user.backend.dto.AnswerDto;
import com.user.backend.mapper.AnswerMapper;
import com.user.backend.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Answer Service 구현체
 */
@Service("answerMybatis")
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerMapper answerMapper;

    /**
     * boardId와 일치하는 문의 게시물에 있는 답변 가져오기
     *
     * @param boardId 문의 게시물 ID
     * @return 해당 게시물의 답변 Optional
     */
    @Override
    public Optional<AnswerDto> getAnswerByBoardId(Long boardId) {
        return answerMapper.selectByBoardId(boardId);
    }

}

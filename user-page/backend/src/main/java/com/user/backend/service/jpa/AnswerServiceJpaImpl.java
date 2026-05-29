package com.user.backend.service.jpa;

import com.user.backend.dto.AnswerDto;
import com.user.backend.repository.AnswerRepository;
import com.user.backend.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Jpa AnswerService Impl
 */
@Service("answerJpa")
@RequiredArgsConstructor
@Transactional
public class AnswerServiceJpaImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;

    /**
     * boardId와 일치하는 문의 게시물에 있는 답변 가져오기
     *
     * @param boardId 문의 게시물 ID
     * @return 해당 게시물의 답변 Optional
     */
    @Override
    public Optional<AnswerDto> getAnswerByBoardId(Long boardId) {
        return answerRepository.findByBoardBoardId(boardId)
                .map(answer -> modelMapper.map(answer, AnswerDto.class));
    }
}

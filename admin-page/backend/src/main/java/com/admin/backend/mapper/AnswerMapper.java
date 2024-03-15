package com.admin.backend.mapper;

import com.admin.backend.dto.AnswerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

/**
 * tb_answer mapper
 */
@Mapper
public interface AnswerMapper {

    /**
     * SELECT tb_answer by boardId
     *
     * @param boardId
     * @return
     */
    Optional<AnswerDto> selectByBoardId(Long boardId);

    /**
     * INSERT tb_answer
     *
     * @param answerDto
     */
    void insertAnswer(AnswerDto answerDto);

    /**
     * DELETE tb_answer by BoardId
     *
     * @param BoardId
     */
    void deleteByBoardId(Long BoardId);

    /**
     * UPDATE tb_answer
     *
     * @param answerDto
     */
    void updateAnswer(AnswerDto answerDto);
}

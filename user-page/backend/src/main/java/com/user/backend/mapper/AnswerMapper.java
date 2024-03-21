package com.user.backend.mapper;

import com.user.backend.dto.AnswerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

/**
 * tb_answer mapper
 */
@Mapper
public interface AnswerMapper {

    /**
     * SELECT tb_answer By boardId
     *
     * @param boardId boardId ( fk )
     * @return boardId와 일치하는 answer
     */
    Optional<AnswerDto> selectByBoardId(Long boardId);

    /**
     * INSERT tb_answer
     *
     * @param answerDto ( boardId, authorId, content )
     */
    void insertAnswer(AnswerDto answerDto);

    /**
     * DELETE tb_answer By BoardId
     *
     * @param BoardId boardId ( fk )
     */
    void deleteByBoardId(Long BoardId);

    /**
     * UPDATE tb_answer
     *
     * @param answerDto ( authorId, content , boardId )
     */
    void updateAnswer(AnswerDto answerDto);
}

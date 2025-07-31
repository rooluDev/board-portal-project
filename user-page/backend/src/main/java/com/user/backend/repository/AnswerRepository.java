package com.user.backend.repository;

import com.user.backend.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    /**
     * InquiryBoard 하나당 하나의 답변만 존재할 경우 단건 조회(Optional)
     *
     */
    Optional<Answer> findByBoardBoardId(Long boardId);

    /**
     * 특정 게시글의 답변 삭제
     *
     */
    void deleteByBoardBoardId(Long boardId);
}

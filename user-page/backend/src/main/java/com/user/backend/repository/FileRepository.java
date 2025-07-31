package com.user.backend.repository;

import com.user.backend.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * File Repository
 */
@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    /**
     * 특정 게시판(boardType, boardId)에 등록된 파일 전체 조회
     */
    List<File> findByBoardTypeAndBoardId(String boardType, Long boardId);

    /**
     * 특정 게시판(boardType, boardId)에 등록된 파일 개수 조회
     */
    long countByBoardTypeAndBoardId(String boardType, Long boardId);

}

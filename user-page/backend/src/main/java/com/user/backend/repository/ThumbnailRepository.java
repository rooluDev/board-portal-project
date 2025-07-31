package com.user.backend.repository;

import com.user.backend.entity.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ThumbnailRepository
 */
@Repository
public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
    /**
     * 특정 파일(fileId)에 연결된 썸네일 목록 조회
     */
    List<Thumbnail> findByFileFileId(Long fileId);

    /**
     * 특정 파일(fileId)에 연결된 썸네일 일괄 삭제
     */
    @Transactional
    @Modifying
    int deleteByFileFileId(Long fileId);

}

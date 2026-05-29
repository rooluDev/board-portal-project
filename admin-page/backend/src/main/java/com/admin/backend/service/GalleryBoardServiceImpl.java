package com.admin.backend.service;

import com.admin.backend.dto.GalleryBoardDto;
import com.admin.backend.dto.SearchConditionDto;
import com.admin.backend.mapper.GalleryBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Gallery Board Service 구현체
 */
@Service
@Primary
@RequiredArgsConstructor
public class GalleryBoardServiceImpl implements GalleryBoardService{

    private final GalleryBoardMapper galleryBoardMapper;

    /**
     * 검색조건에 맞는 갤러리 게시물의 총 개수 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건에 맞는 갤러리 게시물의 총 개수
     */
    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return galleryBoardMapper.selectTotalRowCountByCondition(searchConditionDto);
    }

    /**
     * 검색조건과 페이지네이션에 맞는 갤러리 게시물 리스트 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 갤러리 게시물 리스트
     */
    @Override
    public List<GalleryBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto) {
        return galleryBoardMapper.selectBoardListByCondition(searchConditionDto);
    }

    /**
     * 갤러리 게시물 추가
     *
     * @param galleryBoardDto 등록할 게시물 데이터 (categoryId, authorType, authorId, title, content)
     * @return 생성된 게시물의 pk
     */
    @Override
    public Long addBoard(GalleryBoardDto galleryBoardDto) {
        galleryBoardMapper.insertBoard(galleryBoardDto);
        return galleryBoardDto.getBoardId();
    }

    /**
     * 갤러리 게시물 찾기
     *
     * @param boardId 게시물의 pk
     * @return boardId와 일치하는 갤러리 게시물
     */
    @Override
    public Optional<GalleryBoardDto> getBoardById(Long boardId) {
        return galleryBoardMapper.selectBoardById(boardId);
    }

    /**
     * 갤러리 게시물 삭제 처리 (소프트 딜리트)
     *
     * @param boardId 게시물의 pk
     */
    @Override
    public void deleteBoard(Long boardId) {
        galleryBoardMapper.updateBoardByIdForDelete(boardId);
    }

    /**
     * 조회수 증가
     *
     * @param boardId 게시물의 pk
     */
    @Override
    public void increaseView(Long boardId) {
        galleryBoardMapper.updateView(boardId);
    }

    /**
     * 갤러리 게시물 수정
     *
     * @param galleryBoardDto 수정할 게시물 데이터 (categoryId, title, content, boardId)
     */
    @Override
    public void modifyBoard(GalleryBoardDto galleryBoardDto) {
        galleryBoardMapper.updateBoard(galleryBoardDto);
    }
}

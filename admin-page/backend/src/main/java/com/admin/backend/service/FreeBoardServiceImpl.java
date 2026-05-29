package com.admin.backend.service;

import com.admin.backend.dto.FreeBoardDto;
import com.admin.backend.dto.SearchConditionDto;
import com.admin.backend.mapper.FreeBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Free Board Service 구현체
 */
@Service
@Primary
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService{

    private final FreeBoardMapper freeBoardMapper;

    /**
     * 검색조건에 맞는 자유게시물의 총 개수 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건에 맞는 자유게시물의 총 개수
     */
    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return freeBoardMapper.selectTotalRowCountByCondition(searchConditionDto);
    }

    /**
     * 검색조건과 페이지네이션에 맞는 자유게시물 리스트 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 자유게시물 리스트
     */
    @Override
    public List<FreeBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto) {
        return freeBoardMapper.selectBoardListByCondition(searchConditionDto);
    }

    /**
     * 자유게시물 추가
     *
     * @param freeBoardDto 등록할 게시물 데이터 (categoryId, authorType, authorId, title, content)
     * @return 생성된 게시물의 pk
     */
    @Override
    public Long addBoard(FreeBoardDto freeBoardDto) {
        freeBoardMapper.insertBoard(freeBoardDto);
        return freeBoardDto.getBoardId();
    }

    /**
     * 자유게시물 찾기
     *
     * @param boardId 게시물의 pk
     * @return boardId와 일치하는 자유게시물
     */
    @Override
    public Optional<FreeBoardDto> getBoardById(Long boardId) {
        return freeBoardMapper.selectBoardById(boardId);
    }

    /**
     * 자유게시물 삭제 처리 (소프트 딜리트)
     *
     * @param boardId 게시물의 pk
     */
    @Override
    public void deleteBoard(Long boardId) {
        freeBoardMapper.updateBoardByIdForDelete(boardId);
    }

    /**
     * 조회수 증가
     *
     * @param boardId 게시물의 pk
     */
    @Override
    public void increaseView(Long boardId) {
        freeBoardMapper.updateView(boardId);
    }

    /**
     * 자유게시물 수정
     *
     * @param freeBoardDto 수정할 게시물 데이터 (categoryId, title, content, boardId)
     */
    @Override
    public void modifyBoard(FreeBoardDto freeBoardDto) {
        freeBoardMapper.updateBoard(freeBoardDto);
    }
}

package com.admin.backend.service;

import com.admin.backend.dto.InquiryBoardDto;
import com.admin.backend.dto.SearchConditionDto;
import com.admin.backend.mapper.InquiryBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Inquiry Board Service 구현체
 */
@Service
@Primary
@RequiredArgsConstructor
public class InquiryBoardServiceImpl implements InquiryBoardService{

    private final InquiryBoardMapper inquiryBoardMapper;

    /**
     * 검색조건에 맞는 문의 게시물 총 개수 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건에 맞는 문의 게시물의 총 개수
     */
    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return inquiryBoardMapper.selectTotalRowCountByCondition(searchConditionDto);
    }

    /**
     * 검색조건과 페이지네이션에 맞는 문의 게시물 리스트 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 문의 게시물 리스트
     */
    @Override
    public List<InquiryBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto) {
        return inquiryBoardMapper.selectBoardListByCondition(searchConditionDto);
    }

    /**
     * 문의 게시물 찾기
     *
     * @param boardId 게시물의 pk
     * @return boardId와 일치하는 문의 게시물
     */
    @Override
    public Optional<InquiryBoardDto> getBoardById(Long boardId) {
        return inquiryBoardMapper.selectBoardById(boardId);
    }

    /**
     * 문의 게시물 삭제
     *
     * @param boardId 게시물의 pk
     */
    @Override
    public void deleteBoardById(Long boardId) {
        inquiryBoardMapper.deleteBoardById(boardId);
    }

    /**
     * 조회수 증가
     *
     * @param boardId 게시물의 pk
     */
    @Override
    public void increaseViewById(Long boardId) {
        inquiryBoardMapper.updateViewById(boardId);
    }
}

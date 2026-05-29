package com.admin.backend.service;

import com.admin.backend.common.exception.BoardNotFoundException;
import com.admin.backend.common.exception.FixedBoardFullException;
import com.admin.backend.dto.NoticeBoardDto;
import com.admin.backend.dto.SearchConditionDto;
import com.admin.backend.mapper.NoticeBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Notice Board Service 구현체
 */
@Service
@Primary
@RequiredArgsConstructor
public class NoticeBoardServiceImpl implements NoticeBoardService {

    private final NoticeBoardMapper noticeBoardMapper;

    /**
     * 검색조건과 페이지네이션에 맞는 공지사항 리스트 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 공지사항 리스트
     */
    @Override
    public List<NoticeBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto) {
        return noticeBoardMapper.selectBoardListByCondition(searchConditionDto);
    }

    /**
     * 상단 고정인 공지사항 리스트 가져오기
     *
     * @return 상단 고정인 공지사항 리스트
     */
    @Override
    public List<NoticeBoardDto> getFixedBoardList() {
        return noticeBoardMapper.selectFixedBoardList();
    }

    /**
     * 검색조건에 맞는 공지사항의 총 개수 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건에 맞는 공지사항의 총 개수
     */
    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return noticeBoardMapper.selectTotalRowCountByCondition(searchConditionDto);
    }

    /**
     * 공지사항 추가
     *
     * @param noticeBoardDto 등록할 공지사항 데이터 (categoryId, authorId, title, content, fixed)
     * @throws FixedBoardFullException 고정글이 5개 이상일 경우 발생
     */
    @Override
    public void addBoard(NoticeBoardDto noticeBoardDto) throws FixedBoardFullException {
        noticeBoardMapper.insertBoard(noticeBoardDto);
    }

    /**
     * 공지사항 가져오기
     *
     * @param boardId 게시물의 pk
     * @return boardId와 일치하는 공지사항
     */
    @Override
    public Optional<NoticeBoardDto> getBoardByBoardId(Long boardId) {
        return noticeBoardMapper.selectBoardByBoardId(boardId);
    }

    /**
     * 공지사항 수정
     *
     * @param afterUpdateBoard 수정할 공지사항 데이터 (categoryId, title, content, fixed)
     * @throws BoardNotFoundException   게시물이 없을 경우 발생
     * @throws FixedBoardFullException  고정글이 5개 이상일 경우 발생
     */
    @Override
    public void modifyBoard(NoticeBoardDto afterUpdateBoard) throws BoardNotFoundException, FixedBoardFullException {
        noticeBoardMapper.updateBoard(afterUpdateBoard);
    }

    /**
     * 공지사항 삭제
     *
     * @param boardId 게시물의 pk
     */
    @Override
    public void deleteBoardByBoardId(Long boardId) {
        noticeBoardMapper.deleteBoardByBoardId(boardId);
    }

    /**
     * 조회수 증가
     *
     * @param boardId 게시물의 pk
     */
    @Override
    public void increaseView(Long boardId) {
        noticeBoardMapper.updateView(boardId);
    }

}

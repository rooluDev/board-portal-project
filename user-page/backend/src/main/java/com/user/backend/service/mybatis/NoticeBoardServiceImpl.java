package com.user.backend.service.mybatis;

import com.user.backend.dto.NoticeBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.mapper.NoticeBoardMapper;
import com.user.backend.service.NoticeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Notice Board Service 구현체
 */
@Service("noticeBoardMybatis")
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
     * 공지사항 가져오기
     *
     * @param boardId 게시물 ID (pk)
     * @return boardId와 일치하는 공지사항 Optional
     */
    @Override
    public Optional<NoticeBoardDto> getBoardByBoardId(Long boardId) {
        return noticeBoardMapper.selectBoardByBoardId(boardId);
    }

    /**
     * 공지사항 조회수 1 증가
     *
     * @param boardId 게시물 ID (pk)
     */
    @Override
    public void increaseView(Long boardId) {
        noticeBoardMapper.updateView(boardId);
    }

    /**
     * 메인 페이지에 필요한 공지사항 리스트 가져오기 (최신 6건)
     *
     * @return 메인 페이지에 필요한 공지사항 리스트
     */
    @Override
    public List<NoticeBoardDto> getBoardListForMain() {
        return noticeBoardMapper.selectBoardListForMain();
    }

}

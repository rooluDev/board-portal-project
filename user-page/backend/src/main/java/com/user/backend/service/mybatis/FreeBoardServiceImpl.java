package com.user.backend.service.mybatis;

import com.user.backend.dto.FreeBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.mapper.FreeBoardMapper;
import com.user.backend.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Free Board Service 구현체
 */
@Service("freeBoardMybatis")
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService {

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
     * @param freeBoardDto 추가할 게시물 정보 (categoryId, authorType, authorId, title, content)
     * @return 생성된 게시물의 ID
     */
    @Override
    public Long addBoard(FreeBoardDto freeBoardDto) {
        freeBoardMapper.insertBoard(freeBoardDto);
        return freeBoardDto.getBoardId();
    }

    /**
     * 자유 게시물 찾기
     *
     * @param boardId 게시물 ID (pk)
     * @return boardId와 일치하는 자유 게시물 Optional
     */
    @Override
    public Optional<FreeBoardDto> getBoardById(Long boardId) {
        return freeBoardMapper.selectBoardById(boardId);
    }

    /**
     * 자유게시물 논리 삭제
     *
     * @param boardId 삭제할 게시물 ID (pk)
     */
    @Override
    public void deleteBoard(Long boardId) {
        freeBoardMapper.updateBoardByIdForDelete(boardId);
    }

    /**
     * 자유게시물 조회수 1 증가
     *
     * @param boardId 게시물 ID (pk)
     */
    @Override
    public void increaseView(Long boardId) {
        freeBoardMapper.updateView(boardId);
    }

    /**
     * 자유게시물 수정
     *
     * @param freeBoardDto 수정할 게시물 정보 (categoryId, title, content, boardId)
     */
    @Override
    public void modifyBoard(FreeBoardDto freeBoardDto) {
        freeBoardMapper.updateBoard(freeBoardDto);
    }

    /**
     * 메인 페이지에 필요한 자유 게시판 리스트 가져오기 (최신 6건)
     *
     * @return 메인 페이지에 필요한 자유 게시판 리스트
     */
    @Override
    public List<FreeBoardDto> getBoardListForMain() {
        return freeBoardMapper.selectBoardListForMain();
    }

    /**
     * boardId와 memberId가 일치하는 게시물 가져오기 (작성자 확인용)
     *
     * @param boardId  게시물 ID (pk)
     * @param memberId 작성자 회원 ID
     * @return boardId와 memberId가 일치하는 게시물 Optional
     */
    @Override
    public Optional<FreeBoardDto> getBoardByIdAndMemberId(Long boardId, String memberId) {
        return freeBoardMapper.selectBoardByIdAndMemberId(boardId, memberId);
    }
}

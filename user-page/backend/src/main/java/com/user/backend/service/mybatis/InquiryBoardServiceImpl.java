package com.user.backend.service.mybatis;

import com.user.backend.dto.InquiryBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.mapper.InquiryBoardMapper;
import com.user.backend.service.InquiryBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Inquiry Board Service 구현체
 */
@Service("inquiryBoardMybatis")
@RequiredArgsConstructor
public class InquiryBoardServiceImpl implements InquiryBoardService {

    private final InquiryBoardMapper inquiryBoardMapper;

    /**
     * 검색조건에 맞는 문의 게시물 총 개수 가져오기
     *
     * @param searchConditionDto 검색조건
     * @param memberId           나의 문의 내역 조회 시 사용할 회원 ID (null이면 전체 조회)
     * @return 검색조건에 맞는 문의 게시물 총 개수
     */
    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto, String memberId) {
        return inquiryBoardMapper.selectTotalRowCountByCondition(searchConditionDto, memberId);
    }

    /**
     * 검색조건과 페이지네이션에 맞는 문의 게시물 리스트 가져오기
     *
     * @param searchConditionDto 검색조건
     * @param memberId           나의 문의 내역 조회 시 사용할 회원 ID (null이면 전체 조회)
     * @return 검색조건과 페이지네이션에 맞는 문의 게시물 리스트
     */
    @Override
    public List<InquiryBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto, String memberId) {
        return inquiryBoardMapper.selectBoardListByCondition(searchConditionDto, memberId);
    }

    /**
     * 문의 게시물 찾기
     *
     * @param boardId 게시물 ID (pk)
     * @return boardId와 일치하는 문의 게시물 Optional
     */
    @Override
    public Optional<InquiryBoardDto> getBoardById(Long boardId) {
        return inquiryBoardMapper.selectBoardById(boardId);
    }

    /**
     * 문의 게시물 삭제
     *
     * @param boardId 삭제할 게시물 ID (pk)
     */
    @Override
    public void deleteBoardById(Long boardId) {
        inquiryBoardMapper.deleteBoardById(boardId);
    }

    /**
     * 문의 게시물 조회수 1 증가
     *
     * @param boardId 게시물 ID (pk)
     */
    @Override
    public void increaseView(Long boardId) {
        inquiryBoardMapper.updateViewById(boardId);
    }

    /**
     * 메인 페이지에 필요한 문의 게시판 리스트 가져오기 (최신 6건)
     *
     * @return 메인 페이지에 필요한 문의 게시물 리스트
     */
    @Override
    public List<InquiryBoardDto> getBoardListForMain() {
        return inquiryBoardMapper.selectBoardListForMain();
    }

    /**
     * 문의 게시판 추가
     *
     * @param inquiryBoardDto 추가할 게시물 정보 (authorId, title, content, isSecret)
     */
    @Override
    public void addBoard(InquiryBoardDto inquiryBoardDto) {
        inquiryBoardMapper.insertBoard(inquiryBoardDto);
    }

    /**
     * 문의 게시판 수정
     *
     * @param inquiryBoardDto 수정할 게시물 정보 (title, content, isSecret, boardId)
     */
    @Override
    public void modifyBoard(InquiryBoardDto inquiryBoardDto) {
        inquiryBoardMapper.updateBoard(inquiryBoardDto);
    }

    /**
     * boardId와 memberId가 일치하는 게시물 가져오기 (작성자 확인용)
     *
     * @param boardId  게시물 ID (pk)
     * @param memberId 작성자 회원 ID
     * @return boardId와 memberId가 일치하는 게시물 Optional
     */
    @Override
    public Optional<InquiryBoardDto> getBoardByIdAndMemberId(Long boardId, String memberId) {
        return inquiryBoardMapper.selectBoardByIdAndMemberId(boardId, memberId);
    }
}

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

    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto, String memberId) {
        return inquiryBoardMapper.selectTotalRowCountByCondition(searchConditionDto, memberId);
    }

    @Override
    public List<InquiryBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto, String memberId) {
        return inquiryBoardMapper.selectBoardListByCondition(searchConditionDto, memberId);
    }

    @Override
    public Optional<InquiryBoardDto> getBoardById(Long boardId) {
        return inquiryBoardMapper.selectBoardById(boardId);
    }

    @Override
    public void deleteBoardById(Long boardId) {
        inquiryBoardMapper.deleteBoardById(boardId);
    }

    @Override
    public void increaseView(Long boardId) {
        inquiryBoardMapper.updateViewById(boardId);
    }

    @Override
    public List<InquiryBoardDto> getBoardListForMain() {
        return inquiryBoardMapper.selectBoardListForMain();
    }

    @Override
    public void addBoard(InquiryBoardDto inquiryBoardDto) {
        inquiryBoardMapper.insertBoard(inquiryBoardDto);
    }

    @Override
    public void modifyBoard(InquiryBoardDto inquiryBoardDto) {
        inquiryBoardMapper.updateBoard(inquiryBoardDto);
    }

    @Override
    public Optional<InquiryBoardDto> getBoardByIdAndMemberId(Long boardId, String memberId) {
        return inquiryBoardMapper.selectBoardByIdAndMemberId(boardId, memberId);
    }
}

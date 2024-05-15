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

    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return inquiryBoardMapper.selectTotalRowCountByCondition(searchConditionDto);
    }

    @Override
    public List<InquiryBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto) {
        return inquiryBoardMapper.selectBoardListByCondition(searchConditionDto);
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
    public void increaseViewById(Long boardId) {
        inquiryBoardMapper.updateViewById(boardId);
    }
}

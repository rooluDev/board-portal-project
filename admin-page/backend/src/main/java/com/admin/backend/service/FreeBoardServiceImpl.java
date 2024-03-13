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

    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return freeBoardMapper.selectTotalRowCountByCondition(searchConditionDto);
    }

    @Override
    public List<FreeBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto) {
        return freeBoardMapper.selectBoardListByCondition(searchConditionDto);
    }

    @Override
    public Long addBoard(FreeBoardDto freeBoardDto) {
        freeBoardMapper.insertBoard(freeBoardDto);
        return freeBoardDto.getBoardId();
    }

    @Override
    public Optional<FreeBoardDto> getBoardById(Long boardId) {
        return freeBoardMapper.selectBoardById(boardId);
    }

    @Override
    public void deleteBoard(Long boardId) {
        freeBoardMapper.updateBoardById(boardId);
    }

    @Override
    public void increaseView(Long boardId) {
        freeBoardMapper.updateView(boardId);
    }
}

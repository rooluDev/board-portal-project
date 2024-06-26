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

    @Override
    public List<NoticeBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto) {
        return noticeBoardMapper.selectBoardListByCondition(searchConditionDto);
    }

    @Override
    public List<NoticeBoardDto> getFixedBoardList() {
        return noticeBoardMapper.selectFixedBoardList();
    }

    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return noticeBoardMapper.selectTotalRowCountByCondition(searchConditionDto);
    }

    @Override
    public void addBoard(NoticeBoardDto noticeBoardDto) throws FixedBoardFullException {
        noticeBoardMapper.insertBoard(noticeBoardDto);
    }

    @Override
    public Optional<NoticeBoardDto> getBoardByBoardId(Long boardId) {
        return noticeBoardMapper.selectBoardByBoardId(boardId);
    }

    @Override
    public void modifyBoard(NoticeBoardDto afterUpdateBoard) throws BoardNotFoundException, FixedBoardFullException {
        noticeBoardMapper.updateBoard(afterUpdateBoard);
    }

    @Override
    public void deleteBoardByBoardId(Long boardId) {
        noticeBoardMapper.deleteBoardByBoardId(boardId);
    }

    @Override
    public void increaseView(Long boardId) {
        noticeBoardMapper.updateView(boardId);
    }

}

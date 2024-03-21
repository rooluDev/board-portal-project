package com.user.backend.service;

import com.user.backend.common.exception.BoardNotFoundException;
import com.user.backend.common.exception.FixedBoardFullException;
import com.user.backend.dto.NoticeBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.mapper.NoticeBoardMapper;
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
        if (noticeBoardDto.getFixed() != null) {
            checkFixedCount();
        }
        noticeBoardMapper.insertBoard(noticeBoardDto);
    }

    @Override
    public Optional<NoticeBoardDto> getBoardByBoardId(Long boardId) {
        return noticeBoardMapper.selectBoardByBoardId(boardId);
    }

    @Override
    public void modifyBoard(NoticeBoardDto afterUpdateBoard) throws BoardNotFoundException, FixedBoardFullException {

        NoticeBoardDto beforeUpdateBoard = getBoardByBoardId(afterUpdateBoard.getBoardId()).orElseThrow(() -> new BoardNotFoundException("존재하지 않은 게시물입니다."));

        boolean modifyToFixed = afterUpdateBoard.getFixed() != null && beforeUpdateBoard.getFixed().equals("0");
        if (modifyToFixed) {
            checkFixedCount();
        }

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

    private int getFixedBoardCount() {
        return noticeBoardMapper.selectFixedBoardCount();
    }

    private void checkFixedCount() {
        if (this.getFixedBoardCount() > 4) {
            throw new FixedBoardFullException("상단 고정글이 5개까지 가능합니다.");
        }
    }
}

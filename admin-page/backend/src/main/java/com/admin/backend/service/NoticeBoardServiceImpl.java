package com.admin.backend.service;

import com.admin.backend.common.exception.FixedBoardFullException;
import com.admin.backend.dto.NoticeBoardDto;
import com.admin.backend.dto.SearchConditionDto;
import com.admin.backend.mapper.NoticeBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private int getFixedBoardCount() {
        return noticeBoardMapper.selectFixedBoardCount();
    }

    private void checkFixedCount() {
        if (this.getFixedBoardCount() > 4) {
            throw new FixedBoardFullException("상단 고정글이 5개까지 가능합니다.");
        }
    }
}

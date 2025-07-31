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
    public Optional<NoticeBoardDto> getBoardByBoardId(Long boardId) {
        return noticeBoardMapper.selectBoardByBoardId(boardId);
    }

    @Override
    public void increaseView(Long boardId) {
        noticeBoardMapper.updateView(boardId);
    }

    @Override
    public List<NoticeBoardDto> getBoardListForMain() {
        return noticeBoardMapper.selectBoardListForMain();
    }

}

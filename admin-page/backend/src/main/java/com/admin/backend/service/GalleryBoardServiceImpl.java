package com.admin.backend.service;

import com.admin.backend.dto.GalleryBoardDto;
import com.admin.backend.dto.SearchConditionDto;
import com.admin.backend.mapper.GalleryBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Gallery Board Service 구현체
 */
@Service
@Primary
@RequiredArgsConstructor
public class GalleryBoardServiceImpl implements GalleryBoardService{

    private final GalleryBoardMapper galleryBoardMapper;

    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return galleryBoardMapper.selectTotalRowCountByCondition(searchConditionDto);
    }

    @Override
    public List<GalleryBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto) {
        return galleryBoardMapper.selectBoardListByCondition(searchConditionDto);
    }

    @Override
    public Long addBoard(GalleryBoardDto galleryBoardDto) {
        galleryBoardMapper.insertBoard(galleryBoardDto);
        return galleryBoardDto.getBoardId();
    }

    @Override
    public Optional<GalleryBoardDto> getBoardById(Long boardId) {
        return galleryBoardMapper.selectBoardById(boardId);
    }

    @Override
    public void deleteBoard(Long boardId) {
        galleryBoardMapper.updateBoardByIdForDelete(boardId);
    }

    @Override
    public void increaseView(Long boardId) {
        galleryBoardMapper.updateView(boardId);
    }

    @Override
    public void modifyBoard(GalleryBoardDto galleryBoardDto) {
        galleryBoardMapper.updateBoard(galleryBoardDto);
    }
}

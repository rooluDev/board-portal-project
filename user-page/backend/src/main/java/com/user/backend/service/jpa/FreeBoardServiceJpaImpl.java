package com.user.backend.service.jpa;

import com.user.backend.dto.FreeBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.entity.Category;
import com.user.backend.entity.FreeBoard;
import com.user.backend.repository.CategoryRepository;
import com.user.backend.repository.FreeBoardRepository;
import com.user.backend.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * FreeBoardServiceJpaImpl
 */
@Service("freeBoardJpa")
@RequiredArgsConstructor
@Transactional
public class FreeBoardServiceJpaImpl implements FreeBoardService {

    private final FreeBoardRepository freeBoardRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return (int) freeBoardRepository.findTotalRowCountByCondition(searchConditionDto);
    }

    @Override
    public List<FreeBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto) {
        return freeBoardRepository.findBySearchCondition(searchConditionDto)
                .stream()
                .map(freeBoard -> modelMapper.map(freeBoard, FreeBoardDto.class))
                .toList();
    }

    @Override
    @Transactional
    public Long addBoard(FreeBoardDto freeBoardDto) {
        FreeBoard freeBoard = modelMapper.map(freeBoardDto, FreeBoard.class);
        freeBoard.setCategory(categoryRepository.findById(freeBoardDto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리")));
        return freeBoardRepository.save(freeBoard).getBoardId();
    }

    @Override
    public Optional<FreeBoardDto> getBoardById(Long boardId) {
        return freeBoardRepository.findById(boardId)
                .map(freeBoard -> modelMapper.map(freeBoard, FreeBoardDto.class));
    }

    @Override
    @Transactional
    public void deleteBoard(Long boardId) {
        freeBoardRepository.findById(boardId).ifPresent(freeBoard -> {
            freeBoard.setIsDeleted(true);
            freeBoard.setContent("삭제된 게시물입니다.");
            freeBoard.setEditedAt(Timestamp.valueOf(LocalDateTime.now()));
        });
    }

    @Override
    @Transactional
    public void increaseView(Long boardId) {
        freeBoardRepository.findById(boardId).ifPresent(freeBoard -> freeBoard.setViews(freeBoard.getViews() + 1));
    }

    @Override
    @Transactional
    public void modifyBoard(FreeBoardDto freeBoardDto) {
        freeBoardRepository.findById(freeBoardDto.getBoardId()).ifPresent(freeBoard -> {
            Category category = categoryRepository.findById(freeBoardDto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리"));
            freeBoard.setCategory(category);
            freeBoard.setTitle(freeBoardDto.getTitle());
            freeBoard.setContent(freeBoardDto.getContent());
            freeBoard.setEditedAt(Timestamp.valueOf(LocalDateTime.now()));
        });
    }

    @Override
    public List<FreeBoardDto> getBoardListForMain() {
        return freeBoardRepository.findTop6ByIsDeletedFalseOrderByCreatedAtDesc()
                .stream()
                .map(freeBoard -> modelMapper.map(freeBoard, FreeBoardDto.class))
                .toList();
    }

    @Override
    public Optional<FreeBoardDto> getBoardByIdAndMemberId(Long boardId, String memberId) {
        return freeBoardRepository.findByBoardIdAndAuthorId(boardId,memberId)
                .map(freeBoard -> modelMapper.map(freeBoard, FreeBoardDto.class));
    }
}

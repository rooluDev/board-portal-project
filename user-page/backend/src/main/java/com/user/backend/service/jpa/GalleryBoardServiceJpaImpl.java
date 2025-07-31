package com.user.backend.service.jpa;

import com.user.backend.dto.GalleryBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.entity.Category;
import com.user.backend.entity.GalleryBoard;
import com.user.backend.repository.CategoryRepository;
import com.user.backend.repository.GalleryBoardRepository;
import com.user.backend.service.GalleryBoardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * GalleryBoardServiceJpaImpl
 */
@Service("galleryBoardJpa")
@RequiredArgsConstructor
@Transactional
public class GalleryBoardServiceJpaImpl implements GalleryBoardService {

    private final GalleryBoardRepository galleryBoardRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return (int) galleryBoardRepository.findTotalRowCountByCondition(searchConditionDto);
    }

    @Override
    public List<GalleryBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto) {
        return galleryBoardRepository.findBySearchCondition(searchConditionDto)
                .stream()
                .map(galleryBoard -> modelMapper.map(galleryBoard, GalleryBoardDto.class))
                .toList();
    }

    @Override
    @Transactional
    public Long addBoard(GalleryBoardDto galleryBoardDto) {
        Category category = categoryRepository.findById(galleryBoardDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        GalleryBoard galleryBoard = modelMapper.map(galleryBoardDto, GalleryBoard.class);
        galleryBoard.setCategory(category);
        return galleryBoardRepository.save(galleryBoard).getBoardId();
    }

    @Override
    public Optional<GalleryBoardDto> getBoardById(Long boardId) {
        return galleryBoardRepository.findById(boardId)
                .map(galleryBoard -> modelMapper.map(galleryBoard, GalleryBoardDto.class));
    }

    @Override
    @Transactional
    public void deleteBoard(Long boardId) {
        galleryBoardRepository.findById(boardId).ifPresent(galleryBoard -> {
            galleryBoard.setIsDeleted(true);
            galleryBoard.setContent("삭제된 게시물입니다.");
            galleryBoard.setEditedAt(Timestamp.valueOf(LocalDateTime.now()));
        });
    }

    @Override
    @Transactional
    public void increaseView(Long boardId) {
        galleryBoardRepository.findById(boardId).ifPresent(galleryBoard -> galleryBoard.setViews(galleryBoard.getViews() + 1));
    }

    @Override
    @Transactional
    public void modifyBoard(GalleryBoardDto galleryBoardDto) {
        galleryBoardRepository.findById(galleryBoardDto.getBoardId()).ifPresent(galleryBoard -> {
            Category category = categoryRepository.findById(galleryBoardDto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
            galleryBoard.setCategory(category);
            galleryBoard.setTitle(galleryBoardDto.getTitle());
            galleryBoard.setContent(galleryBoardDto.getContent());
            galleryBoard.setEditedAt(Timestamp.valueOf(LocalDateTime.now()));
        });
    }

    @Override
    public List<GalleryBoardDto> getBoardListForMain() {
        return galleryBoardRepository.findTop6ByIsDeletedFalseOrderByCreatedAtDesc()
                .stream()
                .map(galleryBoard -> modelMapper.map(galleryBoard, GalleryBoardDto.class))
                .toList();
    }

    @Override
    public Optional<GalleryBoardDto> getBoardByIdAndMemberId(Long boardId, String memberId) {
        return galleryBoardRepository.findByBoardIdAndAuthorId(boardId,memberId)
                .map(galleryBoard -> modelMapper.map(galleryBoard, GalleryBoardDto.class));
    }
}


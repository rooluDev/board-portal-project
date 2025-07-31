package com.user.backend.service.jpa;

import com.user.backend.dto.NoticeBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.repository.NoticeBoardRepository;
import com.user.backend.service.NoticeBoardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * NoticeBoardServiceJpaImpl
 */
@Service("noticeBoardJpa")
@RequiredArgsConstructor
@Transactional
public class NoticeBoardServiceJpaImpl implements NoticeBoardService {

    private final NoticeBoardRepository noticeBoardRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<NoticeBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto) {
        return noticeBoardRepository.findBySearchCondition(searchConditionDto)
                .stream()
                .map(noticeBoard -> modelMapper.map(noticeBoard, NoticeBoardDto.class))
                .toList();
    }

    @Override
    public List<NoticeBoardDto> getFixedBoardList() {
        return noticeBoardRepository.findByIsFixedTrueOrderByCreatedAtDesc()
                .stream()
                .map(noticeBoard -> modelMapper.map(noticeBoard, NoticeBoardDto.class))
                .toList();
    }

    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return (int) noticeBoardRepository.findTotalRowCountByCondition(searchConditionDto);
    }

    @Override
    public Optional<NoticeBoardDto> getBoardByBoardId(Long boardId) {
        return noticeBoardRepository.findById(boardId)
                .map(noticeBoard -> modelMapper.map(noticeBoard, NoticeBoardDto.class));
    }

    @Override
    @Transactional
    public void increaseView(Long boardId) {
        noticeBoardRepository.findById(boardId)
                .ifPresent(noticeBoard -> noticeBoard.setViews(noticeBoard.getViews() + 1));
    }

    @Override
    public List<NoticeBoardDto> getBoardListForMain() {
        return noticeBoardRepository.findTop6ByOrderByCreatedAtDesc()
                .stream()
                .map(noticeBoard -> modelMapper.map(noticeBoard, NoticeBoardDto.class))
                .toList();
    }
}

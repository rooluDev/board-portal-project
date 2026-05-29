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

    /**
     * 검색조건과 페이지네이션에 맞는 공지사항 리스트 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 공지사항 리스트
     */
    @Override
    public List<NoticeBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto) {
        return noticeBoardRepository.findBySearchCondition(searchConditionDto)
                .stream()
                .map(noticeBoard -> modelMapper.map(noticeBoard, NoticeBoardDto.class))
                .toList();
    }

    /**
     * 상단 고정인 공지사항 리스트 가져오기
     *
     * @return 상단 고정인 공지사항 리스트
     */
    @Override
    public List<NoticeBoardDto> getFixedBoardList() {
        return noticeBoardRepository.findByIsFixedTrueOrderByCreatedAtDesc()
                .stream()
                .map(noticeBoard -> modelMapper.map(noticeBoard, NoticeBoardDto.class))
                .toList();
    }

    /**
     * 검색조건에 맞는 공지사항의 총 개수 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건에 맞는 공지사항의 총 개수
     */
    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return (int) noticeBoardRepository.findTotalRowCountByCondition(searchConditionDto);
    }

    /**
     * 공지사항 가져오기
     *
     * @param boardId 게시물 ID (pk)
     * @return boardId와 일치하는 공지사항 Optional
     */
    @Override
    public Optional<NoticeBoardDto> getBoardByBoardId(Long boardId) {
        return noticeBoardRepository.findById(boardId)
                .map(noticeBoard -> modelMapper.map(noticeBoard, NoticeBoardDto.class));
    }

    /**
     * 공지사항 조회수 1 증가
     *
     * @param boardId 게시물 ID (pk)
     */
    @Override
    @Transactional
    public void increaseView(Long boardId) {
        noticeBoardRepository.findById(boardId)
                .ifPresent(noticeBoard -> noticeBoard.setViews(noticeBoard.getViews() + 1));
    }

    /**
     * 메인 페이지에 필요한 공지사항 리스트 가져오기 (최신 6건)
     *
     * @return 메인 페이지에 필요한 공지사항 리스트
     */
    @Override
    public List<NoticeBoardDto> getBoardListForMain() {
        return noticeBoardRepository.findTop6ByOrderByCreatedAtDesc()
                .stream()
                .map(noticeBoard -> modelMapper.map(noticeBoard, NoticeBoardDto.class))
                .toList();
    }
}

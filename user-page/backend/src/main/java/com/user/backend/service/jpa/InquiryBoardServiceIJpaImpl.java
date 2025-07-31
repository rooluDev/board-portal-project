package com.user.backend.service.jpa;

import com.user.backend.dto.InquiryBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.entity.InquiryBoard;
import com.user.backend.entity.Member;
import com.user.backend.repository.InquiryBoardRepository;
import com.user.backend.repository.MemberRepository;
import com.user.backend.service.InquiryBoardService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * InquiryBoardServiceIJpaImpl
 */
@Service("inquiryBoardJpa")
@RequiredArgsConstructor
@Transactional
public class InquiryBoardServiceIJpaImpl implements InquiryBoardService {

    private final InquiryBoardRepository inquiryBoardRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto, String memberId) {
        return (int) inquiryBoardRepository.findTotalRowCountByCondition(searchConditionDto, memberId);
    }

    @Override
    public List<InquiryBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto, String memberId) {
        return inquiryBoardRepository.findByCondition(searchConditionDto, memberId)
                .stream()
                .map(inquiryBoard -> modelMapper.map(inquiryBoard, InquiryBoardDto.class))
                .toList();
    }

    @Override
    public Optional<InquiryBoardDto> getBoardById(Long boardId) {
        return inquiryBoardRepository.findById(boardId)
                .map(inquiryBoard -> modelMapper.map(inquiryBoard, InquiryBoardDto.class));
    }

    @Override
    @Transactional
    public void deleteBoardById(Long boardId) {
        inquiryBoardRepository.deleteById(boardId);
    }

    @Override
    @Transactional
    public void increaseView(Long boardId) {
        inquiryBoardRepository.findById(boardId)
                .ifPresent(inquiryBoard -> inquiryBoard.setViews(inquiryBoard.getViews() + 1));
    }

    @Override
    public List<InquiryBoardDto> getBoardListForMain() {
        return inquiryBoardRepository.findTop6ByOrderByCreatedAtDesc()
                .stream()
                .map(inquiryBoard -> modelMapper.map(inquiryBoard, InquiryBoardDto.class))
                .toList();
    }

    @Override
    @Transactional
    public void addBoard(InquiryBoardDto inquiryBoardDto) {
        InquiryBoard inquiryBoard = modelMapper.map(inquiryBoardDto, InquiryBoard.class);
        Member author = memberRepository.findById(inquiryBoardDto.getAuthorId()).orElseThrow(()-> new EntityNotFoundException("member not found"));

        boolean secret = Boolean.parseBoolean(inquiryBoardDto.getIsSecret());
        inquiryBoard.setIsSecret(secret);
        inquiryBoard.setAuthor(author);

        inquiryBoardRepository.save(inquiryBoard);
    }

    @Override
    public void modifyBoard(InquiryBoardDto inquiryBoardDto) {
        inquiryBoardRepository.findById(inquiryBoardDto.getBoardId())
                .ifPresent(inquiryBoard -> {
                    inquiryBoard.setTitle(inquiryBoardDto.getTitle());
                    inquiryBoard.setContent(inquiryBoardDto.getContent());
                    boolean isSecret = Boolean.parseBoolean(inquiryBoardDto.getIsSecret());
                    inquiryBoard.setIsSecret(isSecret);
                });
    }

    @Override
    public Optional<InquiryBoardDto> getBoardByIdAndMemberId(Long boardId, String memberId) {
        return inquiryBoardRepository.findByBoardIdAndAuthorMemberId(boardId, memberId)
                .map(inquiryBoard -> modelMapper.map(inquiryBoard, InquiryBoardDto.class));
    }
}

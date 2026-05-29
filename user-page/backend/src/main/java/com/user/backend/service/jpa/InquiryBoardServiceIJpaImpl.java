package com.user.backend.service.jpa;

import com.user.backend.dto.InquiryBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.entity.InquiryBoard;
import com.user.backend.entity.Member;
import com.user.backend.repository.AnswerRepository;
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
    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;

    /**
     * 검색조건에 맞는 문의 게시물 총 개수 가져오기
     *
     * @param searchConditionDto 검색조건
     * @param memberId           나의 문의 내역 조회 시 사용할 회원 ID (null이면 전체 조회)
     * @return 검색조건에 맞는 문의 게시물 총 개수
     */
    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto, String memberId) {
        return (int) inquiryBoardRepository.findTotalRowCountByCondition(searchConditionDto, memberId);
    }

    /**
     * 검색조건과 페이지네이션에 맞는 문의 게시물 리스트 가져오기
     *
     * @param searchConditionDto 검색조건
     * @param memberId           나의 문의 내역 조회 시 사용할 회원 ID (null이면 전체 조회)
     * @return 검색조건과 페이지네이션에 맞는 문의 게시물 리스트
     */
    @Override
    public List<InquiryBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto, String memberId) {
        return inquiryBoardRepository.findByCondition(searchConditionDto, memberId)
                .stream()
                .map(inquiryBoard -> {
                    InquiryBoardDto dto = modelMapper.map(inquiryBoard, InquiryBoardDto.class);
                    // InquiryBoard 엔티티에 없는 answerId 조회
                    answerRepository.findByBoardBoardId(inquiryBoard.getBoardId())
                            .ifPresent(answer -> dto.setAnswerId(String.valueOf(answer.getAnswerId())));
                    return dto;
                })
                .toList();
    }

    /**
     * 문의 게시물 찾기
     *
     * @param boardId 게시물 ID (pk)
     * @return boardId와 일치하는 문의 게시물 Optional
     */
    @Override
    public Optional<InquiryBoardDto> getBoardById(Long boardId) {
        return inquiryBoardRepository.findById(boardId)
                .map(inquiryBoard -> modelMapper.map(inquiryBoard, InquiryBoardDto.class));
    }

    /**
     * 문의 게시물 삭제
     *
     * @param boardId 삭제할 게시물 ID (pk)
     */
    @Override
    @Transactional
    public void deleteBoardById(Long boardId) {
        inquiryBoardRepository.deleteById(boardId);
    }

    /**
     * 문의 게시물 조회수 1 증가
     *
     * @param boardId 게시물 ID (pk)
     */
    @Override
    @Transactional
    public void increaseView(Long boardId) {
        inquiryBoardRepository.findById(boardId)
                .ifPresent(inquiryBoard -> inquiryBoard.setViews(inquiryBoard.getViews() + 1));
    }

    /**
     * 메인 페이지에 필요한 문의 게시판 리스트 가져오기 (최신 6건)
     *
     * @return 메인 페이지에 필요한 문의 게시물 리스트
     */
    @Override
    public List<InquiryBoardDto> getBoardListForMain() {
        return inquiryBoardRepository.findTop6ByOrderByCreatedAtDesc()
                .stream()
                .map(inquiryBoard -> modelMapper.map(inquiryBoard, InquiryBoardDto.class))
                .toList();
    }

    /**
     * 문의 게시판 추가
     *
     * @param inquiryBoardDto 추가할 게시물 정보 (authorId, title, content, isSecret)
     */
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

    /**
     * 문의 게시판 수정 (제목, 내용, 비밀글 여부 수정)
     *
     * @param inquiryBoardDto 수정할 게시물 정보 (title, content, isSecret, boardId)
     */
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

    /**
     * boardId와 memberId가 일치하는 게시물 가져오기 (작성자 확인용)
     *
     * @param boardId  게시물 ID (pk)
     * @param memberId 작성자 회원 ID
     * @return boardId와 memberId가 일치하는 게시물 Optional
     */
    @Override
    public Optional<InquiryBoardDto> getBoardByIdAndMemberId(Long boardId, String memberId) {
        return inquiryBoardRepository.findByBoardIdAndAuthorMemberId(boardId, memberId)
                .map(inquiryBoard -> modelMapper.map(inquiryBoard, InquiryBoardDto.class));
    }
}

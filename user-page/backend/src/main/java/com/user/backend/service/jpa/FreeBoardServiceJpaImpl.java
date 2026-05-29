package com.user.backend.service.jpa;

import com.user.backend.common.type.Author;
import com.user.backend.common.type.Board;
import com.user.backend.dto.FreeBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.entity.Category;
import com.user.backend.entity.FreeBoard;
import com.user.backend.repository.AdminRepository;
import com.user.backend.repository.CategoryRepository;
import com.user.backend.repository.CommentRepository;
import com.user.backend.repository.FileRepository;
import com.user.backend.repository.FreeBoardRepository;
import com.user.backend.repository.MemberRepository;
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
    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;

    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return (int) freeBoardRepository.findTotalRowCountByCondition(searchConditionDto);
    }

    @Override
    public List<FreeBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto) {
        return freeBoardRepository.findBySearchCondition(searchConditionDto)
                .stream()
                .map(freeBoard -> {
                    FreeBoardDto dto = modelMapper.map(freeBoard, FreeBoardDto.class);
                    // FreeBoard 엔티티에 없는 등록자 이름 조회
                    if (Author.ADMIN.getAuthorType().equals(freeBoard.getAuthorType())) {
                        adminRepository.findById(freeBoard.getAuthorId())
                                .ifPresent(admin -> dto.setAdminName(admin.getAdminName()));
                    } else if (Author.MEMBER.getAuthorType().equals(freeBoard.getAuthorType())) {
                        memberRepository.findById(freeBoard.getAuthorId())
                                .ifPresent(member -> dto.setMemberName(member.getMemberName()));
                    }
                    // FreeBoard 엔티티에 없는 댓글 수 조회
                    dto.setCommentCount((int) commentRepository.countByBoardTypeAndBoardId(
                            Board.FREE_BOARD.getBoardType(), freeBoard.getBoardId()));
                    // FreeBoard 엔티티에 없는 첨부파일 여부 조회 (첫 번째 파일 ID)
                    fileRepository.findByBoardTypeAndBoardId(Board.FREE_BOARD.getBoardType(), freeBoard.getBoardId())
                            .stream().findFirst()
                            .ifPresent(file -> dto.setFileId(file.getFileId()));
                    return dto;
                })
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

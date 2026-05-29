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

    /**
     * 검색조건에 맞는 자유게시물의 총 개수 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건에 맞는 자유게시물의 총 개수
     */
    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return (int) freeBoardRepository.findTotalRowCountByCondition(searchConditionDto);
    }

    /**
     * 검색조건과 페이지네이션에 맞는 자유게시물 리스트 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 자유게시물 리스트
     */
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

    /**
     * 자유게시물 추가
     *
     * @param freeBoardDto 추가할 게시물 정보 (categoryId, authorType, authorId, title, content)
     * @return 생성된 게시물의 ID
     */
    @Override
    @Transactional
    public Long addBoard(FreeBoardDto freeBoardDto) {
        FreeBoard freeBoard = modelMapper.map(freeBoardDto, FreeBoard.class);
        freeBoard.setCategory(categoryRepository.findById(freeBoardDto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리")));
        return freeBoardRepository.save(freeBoard).getBoardId();
    }

    /**
     * 자유 게시물 찾기
     *
     * @param boardId 게시물 ID (pk)
     * @return boardId와 일치하는 자유 게시물 Optional
     */
    @Override
    public Optional<FreeBoardDto> getBoardById(Long boardId) {
        return freeBoardRepository.findById(boardId)
                .map(freeBoard -> modelMapper.map(freeBoard, FreeBoardDto.class));
    }

    /**
     * 자유게시물 논리 삭제 (is_deleted = true, content = '삭제된 게시물입니다.')
     *
     * @param boardId 삭제할 게시물 ID (pk)
     */
    @Override
    @Transactional
    public void deleteBoard(Long boardId) {
        freeBoardRepository.findById(boardId).ifPresent(freeBoard -> {
            freeBoard.setIsDeleted(true);
            freeBoard.setContent("삭제된 게시물입니다.");
            freeBoard.setEditedAt(Timestamp.valueOf(LocalDateTime.now()));
        });
    }

    /**
     * 자유게시물 조회수 1 증가
     *
     * @param boardId 게시물 ID (pk)
     */
    @Override
    @Transactional
    public void increaseView(Long boardId) {
        freeBoardRepository.findById(boardId).ifPresent(freeBoard -> freeBoard.setViews(freeBoard.getViews() + 1));
    }

    /**
     * 자유게시물 수정 (카테고리, 제목, 내용 수정)
     *
     * @param freeBoardDto 수정할 게시물 정보 (categoryId, title, content, boardId)
     */
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

    /**
     * 메인 페이지에 필요한 자유 게시판 리스트 가져오기 (삭제되지 않은 최신 6건)
     *
     * @return 메인 페이지에 필요한 자유 게시판 리스트
     */
    @Override
    public List<FreeBoardDto> getBoardListForMain() {
        return freeBoardRepository.findTop6ByIsDeletedFalseOrderByCreatedAtDesc()
                .stream()
                .map(freeBoard -> modelMapper.map(freeBoard, FreeBoardDto.class))
                .toList();
    }

    /**
     * boardId와 memberId가 일치하는 게시물 가져오기 (작성자 확인용)
     *
     * @param boardId  게시물 ID (pk)
     * @param memberId 작성자 회원 ID
     * @return boardId와 memberId가 일치하는 게시물 Optional
     */
    @Override
    public Optional<FreeBoardDto> getBoardByIdAndMemberId(Long boardId, String memberId) {
        return freeBoardRepository.findByBoardIdAndAuthorId(boardId,memberId)
                .map(freeBoard -> modelMapper.map(freeBoard, FreeBoardDto.class));
    }
}

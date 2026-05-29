package com.user.backend.service.jpa;

import com.user.backend.common.type.Author;
import com.user.backend.common.type.Board;
import com.user.backend.dto.GalleryBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.entity.Category;
import com.user.backend.entity.GalleryBoard;
import com.user.backend.entity.Thumbnail;
import com.user.backend.repository.AdminRepository;
import com.user.backend.repository.CategoryRepository;
import com.user.backend.repository.FileRepository;
import com.user.backend.repository.GalleryBoardRepository;
import com.user.backend.repository.MemberRepository;
import com.user.backend.repository.ThumbnailRepository;
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
    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;
    private final ThumbnailRepository thumbnailRepository;
    private final ModelMapper modelMapper;

    /**
     * 검색조건에 맞는 갤러리 게시물의 총 개수 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건에 맞는 갤러리 게시물의 총 개수
     */
    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return (int) galleryBoardRepository.findTotalRowCountByCondition(searchConditionDto);
    }

    /**
     * 검색조건과 페이지네이션에 맞는 갤러리 게시물 리스트 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 갤러리 게시물 리스트
     */
    @Override
    public List<GalleryBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto) {
        return galleryBoardRepository.findBySearchCondition(searchConditionDto)
                .stream()
                .map(galleryBoard -> {
                    GalleryBoardDto dto = modelMapper.map(galleryBoard, GalleryBoardDto.class);
                    // GalleryBoard 엔티티에 없는 등록자 이름을 별도로 조회해서 세팅
                    if (Author.ADMIN.getAuthorType().equals(galleryBoard.getAuthorType())) {
                        adminRepository.findById(galleryBoard.getAuthorId())
                                .ifPresent(admin -> dto.setAdminName(admin.getAdminName()));
                    } else if (Author.MEMBER.getAuthorType().equals(galleryBoard.getAuthorType())) {
                        memberRepository.findById(galleryBoard.getAuthorId())
                                .ifPresent(member -> dto.setMemberName(member.getMemberName()));
                    }
                    // GalleryBoard 엔티티에 없는 thumbnailId 조회
                    setThumbnailId(dto, galleryBoard.getBoardId());
                    return dto;
                })
                .toList();
    }

    /**
     * 갤러리 게시물 추가
     *
     * @param galleryBoardDto 추가할 게시물 정보 (categoryId, authorType, authorId, title, content)
     * @return 생성된 게시물의 ID
     */
    @Override
    @Transactional
    public Long addBoard(GalleryBoardDto galleryBoardDto) {
        Category category = categoryRepository.findById(galleryBoardDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        GalleryBoard galleryBoard = modelMapper.map(galleryBoardDto, GalleryBoard.class);
        galleryBoard.setCategory(category);
        return galleryBoardRepository.save(galleryBoard).getBoardId();
    }

    /**
     * 갤러리 게시물 찾기
     *
     * @param boardId 게시물 ID (pk)
     * @return boardId와 일치하는 갤러리 게시물 Optional
     */
    @Override
    public Optional<GalleryBoardDto> getBoardById(Long boardId) {
        return galleryBoardRepository.findById(boardId)
                .map(galleryBoard -> modelMapper.map(galleryBoard, GalleryBoardDto.class));
    }

    /**
     * 갤러리 게시물 논리 삭제 (is_deleted = true, content = '삭제된 게시물입니다.')
     *
     * @param boardId 삭제할 게시물 ID (pk)
     */
    @Override
    @Transactional
    public void deleteBoard(Long boardId) {
        galleryBoardRepository.findById(boardId).ifPresent(galleryBoard -> {
            galleryBoard.setIsDeleted(true);
            galleryBoard.setContent("삭제된 게시물입니다.");
            galleryBoard.setEditedAt(Timestamp.valueOf(LocalDateTime.now()));
        });
    }

    /**
     * 갤러리 게시물 조회수 1 증가
     *
     * @param boardId 게시물 ID (pk)
     */
    @Override
    @Transactional
    public void increaseView(Long boardId) {
        galleryBoardRepository.findById(boardId).ifPresent(galleryBoard -> galleryBoard.setViews(galleryBoard.getViews() + 1));
    }

    /**
     * 갤러리 게시물 수정 (카테고리, 제목, 내용 수정)
     *
     * @param galleryBoardDto 수정할 게시물 정보 (categoryId, title, content, boardId)
     */
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

    /**
     * 메인 페이지에 필요한 갤러리 리스트 가져오기 (삭제되지 않은 최신 3건)
     *
     * @return 메인 페이지에 필요한 갤러리 게시물 리스트
     */
    @Override
    public List<GalleryBoardDto> getBoardListForMain() {
        return galleryBoardRepository.findTop3ByIsDeletedFalseOrderByCreatedAtDesc()
                .stream()
                .map(galleryBoard -> {
                    GalleryBoardDto dto = modelMapper.map(galleryBoard, GalleryBoardDto.class);
                    setThumbnailId(dto, galleryBoard.getBoardId());
                    return dto;
                })
                .toList();
    }

    /**
     * 게시물의 첫 번째 파일에 연결된 thumbnailId를 DTO에 세팅
     */
    private void setThumbnailId(GalleryBoardDto dto, Long boardId) {
        fileRepository.findByBoardTypeAndBoardId(Board.GALLERY_BOARD.getBoardType(), boardId)
                .stream()
                .findFirst()
                .ifPresent(file -> {
                    thumbnailRepository.findByFileFileId(file.getFileId())
                            .stream()
                            .findFirst()
                            .ifPresent(thumbnail -> dto.setThumbnailId(thumbnail.getThumbnailId()));
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
    public Optional<GalleryBoardDto> getBoardByIdAndMemberId(Long boardId, String memberId) {
        return galleryBoardRepository.findByBoardIdAndAuthorId(boardId,memberId)
                .map(galleryBoard -> modelMapper.map(galleryBoard, GalleryBoardDto.class));
    }
}


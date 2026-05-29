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

    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return (int) galleryBoardRepository.findTotalRowCountByCondition(searchConditionDto);
    }

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

    @Override
    public Optional<GalleryBoardDto> getBoardByIdAndMemberId(Long boardId, String memberId) {
        return galleryBoardRepository.findByBoardIdAndAuthorId(boardId,memberId)
                .map(galleryBoard -> modelMapper.map(galleryBoard, GalleryBoardDto.class));
    }
}


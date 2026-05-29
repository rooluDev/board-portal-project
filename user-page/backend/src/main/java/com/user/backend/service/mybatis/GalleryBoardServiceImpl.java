package com.user.backend.service.mybatis;

import com.user.backend.dto.GalleryBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.mapper.GalleryBoardMapper;
import com.user.backend.service.GalleryBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Gallery Board Service Impl
 */
@Service("galleryBoardMybatis")
@RequiredArgsConstructor
public class GalleryBoardServiceImpl implements GalleryBoardService {

    private final GalleryBoardMapper galleryBoardMapper;

    /**
     * 검색조건에 맞는 갤러리 게시물의 총 개수 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건에 맞는 갤러리 게시물의 총 개수
     */
    @Override
    public int getTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        return galleryBoardMapper.selectTotalRowCountByCondition(searchConditionDto);
    }

    /**
     * 검색조건과 페이지네이션에 맞는 갤러리 게시물 리스트 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 갤러리 게시물 리스트
     */
    @Override
    public List<GalleryBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto) {
        return galleryBoardMapper.selectBoardListByCondition(searchConditionDto);
    }

    /**
     * 갤러리 게시물 추가
     *
     * @param galleryBoardDto 추가할 게시물 정보 (categoryId, authorType, authorId, title, content)
     * @return 생성된 게시물의 ID
     */
    @Override
    public Long addBoard(GalleryBoardDto galleryBoardDto) {
        galleryBoardMapper.insertBoard(galleryBoardDto);
        return galleryBoardDto.getBoardId();
    }

    /**
     * 갤러리 게시물 찾기
     *
     * @param boardId 게시물 ID (pk)
     * @return boardId와 일치하는 갤러리 게시물 Optional
     */
    @Override
    public Optional<GalleryBoardDto> getBoardById(Long boardId) {
        return galleryBoardMapper.selectBoardById(boardId);
    }

    /**
     * 갤러리 게시물 논리 삭제
     *
     * @param boardId 삭제할 게시물 ID (pk)
     */
    @Override
    public void deleteBoard(Long boardId) {
        galleryBoardMapper.updateBoardByIdForDelete(boardId);
    }

    /**
     * 갤러리 게시물 조회수 1 증가
     *
     * @param boardId 게시물 ID (pk)
     */
    @Override
    public void increaseView(Long boardId) {
        galleryBoardMapper.updateView(boardId);
    }

    /**
     * 갤러리 게시물 수정
     *
     * @param galleryBoardDto 수정할 게시물 정보 (categoryId, title, content, boardId)
     */
    @Override
    public void modifyBoard(GalleryBoardDto galleryBoardDto) {
        galleryBoardMapper.updateBoard(galleryBoardDto);
    }

    /**
     * 메인 페이지에 필요한 갤러리 리스트 가져오기 (최신 3건)
     *
     * @return 메인 페이지에 필요한 갤러리 게시물 리스트
     */
    @Override
    public List<GalleryBoardDto> getBoardListForMain() {
        return galleryBoardMapper.selectBoardListForMain();
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
        return galleryBoardMapper.selectBoardByIdAndMemberId(boardId, memberId);
    }
}

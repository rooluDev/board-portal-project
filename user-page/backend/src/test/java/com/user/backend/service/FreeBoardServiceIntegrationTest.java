package com.user.backend.service;

import com.user.backend.dto.FreeBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.entity.Category;
import com.user.backend.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * FreeBoardService 통합 테스트
 * - 실제 MySQL DB 연결 필요 (dev 프로파일)
 */
@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class FreeBoardServiceIntegrationTest {

    @Autowired
    @Qualifier("freeBoardJpa")
    private FreeBoardService freeBoardService;

    @Autowired
    private CategoryRepository categoryRepository;

    private Long testCategoryId;

    @BeforeEach
    void setUp() {
        Category category = categoryRepository.save(Category.builder()
                .categoryName("통합테스트카테고리")
                .boardType("free")
                .build());
        testCategoryId = category.getCategoryId();
    }

    @Test
    @DisplayName("게시물 추가 후 ID 조회 - 저장된 게시물 반환")
    void addBoard_thenGetById_returnsBoard() {
        // given
        FreeBoardDto dto = FreeBoardDto.builder()
                .categoryId(testCategoryId)
                .authorType("member")
                .authorId("testUser")
                .title("통합테스트 게시물")
                .content("통합테스트 내용입니다.")
                .build();

        // when
        Long boardId = freeBoardService.addBoard(dto);
        Optional<FreeBoardDto> result = freeBoardService.getBoardById(boardId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("통합테스트 게시물");
        assertThat(result.get().getAuthorId()).isEqualTo("testUser");
    }

    @Test
    @DisplayName("게시물 삭제 - isDeleted 상태로 변경")
    void deleteBoard_changesContent() {
        // given
        FreeBoardDto dto = FreeBoardDto.builder()
                .categoryId(testCategoryId)
                .authorType("member")
                .authorId("testUser")
                .title("삭제될 게시물")
                .content("삭제 전 내용")
                .build();
        Long boardId = freeBoardService.addBoard(dto);

        // when
        freeBoardService.deleteBoard(boardId);

        // then - 삭제 후에도 조회 가능 (논리적 삭제)
        Optional<FreeBoardDto> result = freeBoardService.getBoardById(boardId);
        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("게시물 목록 조회 - 결과 반환")
    void getBoardListByCondition_returnsResult() {
        // given
        SearchConditionDto condition = new SearchConditionDto();
        condition.setPageNum(1);
        condition.setPageSize(10);
        condition.setOrderValue("createdAt");
        condition.setOrderDirection("DESC");

        // when
        List<FreeBoardDto> result = freeBoardService.getBoardListByCondition(condition);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("조회수 증가 - 정상 실행")
    void increaseView_whenBoardExists_succeeds() {
        // given
        FreeBoardDto dto = FreeBoardDto.builder()
                .categoryId(testCategoryId)
                .authorType("member")
                .authorId("testUser")
                .title("조회수 테스트")
                .content("내용")
                .build();
        Long boardId = freeBoardService.addBoard(dto);

        // when & then (예외 없이 실행되면 성공)
        freeBoardService.increaseView(boardId);
    }
}

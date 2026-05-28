package com.user.backend.repository;

import com.user.backend.entity.Category;
import com.user.backend.entity.FreeBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * FreeBoardRepository 단위 테스트
 * - 실제 MySQL DB 연결 필요 (dev 프로파일)
 * - @Transactional 로 각 테스트 후 롤백
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
@Transactional
class FreeBoardRepositoryTest {

    @Autowired
    private FreeBoardRepository freeBoardRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = categoryRepository.save(Category.builder()
                .categoryName("테스트카테고리")
                .boardType("free")
                .build());
    }

    @Test
    @DisplayName("삭제되지 않은 최신 게시물 최대 6건 조회")
    void findTop6ByIsDeletedFalse_returnsAtMost6() {
        // given - 7건 저장
        for (int i = 1; i <= 7; i++) {
            freeBoardRepository.save(FreeBoard.builder()
                    .category(testCategory)
                    .authorType("member")
                    .authorId("user1")
                    .title("게시물 " + i)
                    .content("내용")
                    .views(0L)
                    .isDeleted(false)
                    .build());
        }

        // when
        List<FreeBoard> result = freeBoardRepository.findTop6ByIsDeletedFalseOrderByCreatedAtDesc();

        // then
        assertThat(result).hasSizeLessThanOrEqualTo(6);
    }

    @Test
    @DisplayName("삭제된 게시물은 조회 결과에서 제외")
    void findTop6ByIsDeletedFalse_excludesDeletedBoards() {
        // given - 삭제된 게시물만 저장
        freeBoardRepository.save(FreeBoard.builder()
                .category(testCategory)
                .authorType("member")
                .authorId("user1")
                .title("삭제된 게시물")
                .content("내용")
                .views(0L)
                .isDeleted(true)
                .build());

        // when
        List<FreeBoard> result = freeBoardRepository.findTop6ByIsDeletedFalseOrderByCreatedAtDesc();

        // then - 반환된 게시물은 모두 삭제되지 않은 것
        result.forEach(board -> assertThat(board.getIsDeleted()).isFalse());
    }

    @Test
    @DisplayName("게시물 ID와 작성자 ID 일치 시 게시물 반환")
    void findByBoardIdAndAuthorId_whenMatch_returnsBoard() {
        // given
        FreeBoard board = freeBoardRepository.save(FreeBoard.builder()
                .category(testCategory)
                .authorType("member")
                .authorId("authorUser")
                .title("내 게시물")
                .content("내용")
                .views(0L)
                .isDeleted(false)
                .build());

        // when
        Optional<FreeBoard> result = freeBoardRepository.findByBoardIdAndAuthorId(board.getBoardId(), "authorUser");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("내 게시물");
    }

    @Test
    @DisplayName("작성자 ID 불일치 시 빈 Optional 반환")
    void findByBoardIdAndAuthorId_whenAuthorMismatch_returnsEmpty() {
        // given
        FreeBoard board = freeBoardRepository.save(FreeBoard.builder()
                .category(testCategory)
                .authorType("member")
                .authorId("otherUser")
                .title("다른 사람 게시물")
                .content("내용")
                .views(0L)
                .isDeleted(false)
                .build());

        // when
        Optional<FreeBoard> result = freeBoardRepository.findByBoardIdAndAuthorId(board.getBoardId(), "notTheAuthor");

        // then
        assertThat(result).isEmpty();
    }
}

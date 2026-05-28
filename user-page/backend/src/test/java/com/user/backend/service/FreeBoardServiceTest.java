package com.user.backend.service;

import com.user.backend.dto.FreeBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.mapper.FreeBoardMapper;
import com.user.backend.service.mybatis.FreeBoardServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FreeBoardServiceTest {

    @Mock
    private FreeBoardMapper freeBoardMapper;

    @InjectMocks
    private FreeBoardServiceImpl freeBoardService;

    @Test
    @DisplayName("게시물 ID로 조회 - 존재하는 경우 게시물 반환")
    void getBoardById_whenExists_returnsBoard() {
        // given
        Long boardId = 1L;
        FreeBoardDto expected = FreeBoardDto.builder()
                .boardId(boardId)
                .title("테스트 제목")
                .content("테스트 내용")
                .build();
        when(freeBoardMapper.selectBoardById(boardId)).thenReturn(Optional.of(expected));

        // when
        Optional<FreeBoardDto> result = freeBoardService.getBoardById(boardId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("테스트 제목");
    }

    @Test
    @DisplayName("게시물 ID로 조회 - 존재하지 않는 경우 빈 Optional 반환")
    void getBoardById_whenNotExists_returnsEmpty() {
        // given
        Long boardId = 999L;
        when(freeBoardMapper.selectBoardById(boardId)).thenReturn(Optional.empty());

        // when
        Optional<FreeBoardDto> result = freeBoardService.getBoardById(boardId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("게시물 등록 - 등록 후 boardId 반환")
    void addBoard_returnsBoardId() {
        // given
        FreeBoardDto freeBoardDto = FreeBoardDto.builder()
                .boardId(1L)
                .categoryId(1L)
                .authorType("member")
                .authorId("user1")
                .title("새 게시물")
                .content("내용")
                .build();
        doNothing().when(freeBoardMapper).insertBoard(freeBoardDto);

        // when
        Long boardId = freeBoardService.addBoard(freeBoardDto);

        // then
        assertThat(boardId).isEqualTo(1L);
        verify(freeBoardMapper, times(1)).insertBoard(freeBoardDto);
    }

    @Test
    @DisplayName("게시물 목록 조회 - 검색 조건에 맞는 리스트 반환")
    void getBoardList_returnsListByCondition() {
        // given
        SearchConditionDto condition = new SearchConditionDto();
        List<FreeBoardDto> expected = List.of(
                FreeBoardDto.builder().boardId(1L).title("첫번째").build(),
                FreeBoardDto.builder().boardId(2L).title("두번째").build()
        );
        when(freeBoardMapper.selectBoardListByCondition(condition)).thenReturn(expected);

        // when
        List<FreeBoardDto> result = freeBoardService.getBoardListByCondition(condition);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("첫번째");
    }

    @Test
    @DisplayName("게시물 삭제 - mapper의 delete 메서드 호출")
    void deleteBoard_callsMapper() {
        // given
        Long boardId = 1L;
        doNothing().when(freeBoardMapper).updateBoardByIdForDelete(boardId);

        // when
        freeBoardService.deleteBoard(boardId);

        // then
        verify(freeBoardMapper, times(1)).updateBoardByIdForDelete(boardId);
    }
}

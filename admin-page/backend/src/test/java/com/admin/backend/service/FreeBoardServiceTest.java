package com.admin.backend.service;

import com.admin.backend.dto.FreeBoardDto;
import com.admin.backend.dto.SearchConditionDto;
import com.admin.backend.mapper.FreeBoardMapper;
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
                .title("공지사항")
                .content("내용입니다.")
                .build();
        when(freeBoardMapper.selectBoardById(boardId)).thenReturn(Optional.of(expected));

        // when
        Optional<FreeBoardDto> result = freeBoardService.getBoardById(boardId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("공지사항");
    }

    @Test
    @DisplayName("게시물 등록 - 등록 후 boardId 반환")
    void addBoard_returnsBoardId() {
        // given
        FreeBoardDto freeBoardDto = FreeBoardDto.builder()
                .boardId(5L)
                .categoryId(1L)
                .authorType("admin")
                .authorId("admin")
                .title("새 게시물")
                .content("내용")
                .build();
        doNothing().when(freeBoardMapper).insertBoard(freeBoardDto);

        // when
        Long boardId = freeBoardService.addBoard(freeBoardDto);

        // then
        assertThat(boardId).isEqualTo(5L);
        verify(freeBoardMapper, times(1)).insertBoard(freeBoardDto);
    }

    @Test
    @DisplayName("게시물 목록 조회 - 검색 조건에 맞는 리스트 반환")
    void getBoardList_returnsListByCondition() {
        // given
        SearchConditionDto condition = new SearchConditionDto();
        List<FreeBoardDto> expected = List.of(
                FreeBoardDto.builder().boardId(1L).title("첫번째 게시물").build(),
                FreeBoardDto.builder().boardId(2L).title("두번째 게시물").build(),
                FreeBoardDto.builder().boardId(3L).title("세번째 게시물").build()
        );
        when(freeBoardMapper.selectBoardListByCondition(condition)).thenReturn(expected);

        // when
        List<FreeBoardDto> result = freeBoardService.getBoardListByCondition(condition);

        // then
        assertThat(result).hasSize(3);
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

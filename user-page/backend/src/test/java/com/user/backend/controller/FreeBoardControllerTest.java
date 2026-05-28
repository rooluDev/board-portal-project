package com.user.backend.controller;

import com.user.backend.dto.FreeBoardDto;
import com.user.backend.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class FreeBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "freeBoardJpa")
    private FreeBoardService freeBoardService;

    @MockBean(name = "categoryJpa")
    private CategoryService categoryService;

    @MockBean(name = "fileJpa")
    private FileService fileService;

    @MockBean(name = "commentJpa")
    private CommentService commentService;

    @MockBean
    private JwtService jwtService;

    @Test
    @DisplayName("게시물 목록 조회 - 200 반환")
    void getBoardList_returnsOk() throws Exception {
        // given
        when(freeBoardService.getTotalRowCountByCondition(any())).thenReturn(1);
        when(freeBoardService.getBoardListByCondition(any())).thenReturn(
                List.of(FreeBoardDto.builder().boardId(1L).title("첫번째 게시물").build())
        );
        when(categoryService.getCategoryListByBoardType(any())).thenReturn(List.of());

        // when & then
        mockMvc.perform(get("/api/boards/free"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시물 단건 조회 - 존재하는 경우 200 반환")
    void getBoard_whenExists_returnsOk() throws Exception {
        // given
        FreeBoardDto freeBoard = FreeBoardDto.builder()
                .boardId(1L).title("테스트 게시물").content("내용").build();
        when(freeBoardService.getBoardById(1L)).thenReturn(Optional.of(freeBoard));
        when(commentService.getCommentListByBoardId(anyLong(), any())).thenReturn(List.of());
        when(fileService.getFileListByBoardId(anyLong(), any())).thenReturn(List.of());
        when(categoryService.getCategoryListByBoardType(any())).thenReturn(List.of());

        // when & then
        mockMvc.perform(get("/api/board/free/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시물 단건 조회 - 존재하지 않는 경우 404 반환")
    void getBoard_whenNotExists_returns404() throws Exception {
        // given
        when(freeBoardService.getBoardById(999L)).thenReturn(Optional.empty());

        // when & then
        mockMvc.perform(get("/api/board/free/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("게시물 조회수 증가 - 200 반환")
    void increaseView_whenExists_returnsOk() throws Exception {
        // given
        FreeBoardDto freeBoard = FreeBoardDto.builder().boardId(1L).build();
        when(freeBoardService.getBoardById(1L)).thenReturn(Optional.of(freeBoard));
        doNothing().when(freeBoardService).increaseView(1L);

        // when & then
        mockMvc.perform(patch("/api/board/free/1/increase-view"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시물 삭제 - JWT 토큰 있는 경우 200 반환")
    void deleteBoard_withToken_returnsOk() throws Exception {
        // given
        when(jwtService.getMemberIdFromToken(any())).thenReturn("user1234");
        doNothing().when(freeBoardService).deleteBoard(1L);

        // when & then
        mockMvc.perform(delete("/api/board/free/1")
                        .header("Authorization", "Bearer sometoken"))
                .andExpect(status().isOk());
    }
}

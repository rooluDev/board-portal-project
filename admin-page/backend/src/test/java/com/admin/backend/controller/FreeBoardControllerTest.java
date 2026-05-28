package com.admin.backend.controller;

import com.admin.backend.dto.AdminDto;
import com.admin.backend.dto.FreeBoardDto;
import com.admin.backend.service.*;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class FreeBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private FreeBoardService freeBoardService;

    @MockBean
    private FileService fileService;

    @MockBean
    private CommentService commentService;

    private AdminDto adminDto;

    @BeforeEach
    void setUp() {
        adminDto = new AdminDto();
        adminDto.setAdminId("admin");
        adminDto.setAdminName("관리자");
    }

    @Test
    @DisplayName("자유게시판 목록 페이지 - 200 반환")
    void getListPage_withSession_returnsOk() throws Exception {
        // given
        when(freeBoardService.getTotalRowCountByCondition(any())).thenReturn(0);
        when(freeBoardService.getBoardListByCondition(any())).thenReturn(List.of());
        when(categoryService.getCategoryListByBoardType(any())).thenReturn(List.of());

        // when & then (세션 속성으로 인터셉터 통과)
        mockMvc.perform(get("/board/free")
                        .sessionAttr(LoginController.ADMIN_SESSION_ID, adminDto))
                .andExpect(status().isOk())
                .andExpect(view().name("board/free/free-list"));
    }

    @Test
    @DisplayName("자유게시판 쓰기 페이지 - 200 반환")
    void getWritePage_withSession_returnsOk() throws Exception {
        // given
        when(categoryService.getCategoryListByBoardType(any())).thenReturn(List.of());

        // when & then
        mockMvc.perform(get("/board/free/write")
                        .sessionAttr(LoginController.ADMIN_SESSION_ID, adminDto))
                .andExpect(status().isOk())
                .andExpect(view().name("board/free/free-write"));
    }

    @Test
    @DisplayName("자유게시판 상세 보기 페이지 - 200 반환")
    void getBoardPage_whenExists_returnsOk() throws Exception {
        // given
        FreeBoardDto board = FreeBoardDto.builder()
                .boardId(1L).title("테스트 게시물").content("내용").build();
        when(freeBoardService.getBoardById(1L)).thenReturn(Optional.of(board));
        when(categoryService.getCategoryListByBoardType(any())).thenReturn(List.of());
        when(fileService.getFileListByBoardId(anyLong(), any())).thenReturn(List.of());
        when(commentService.getCommentListByBoardId(anyLong(), any())).thenReturn(List.of());
        doNothing().when(freeBoardService).increaseView(1L);

        // when & then
        mockMvc.perform(get("/board/free/1")
                        .sessionAttr(LoginController.ADMIN_SESSION_ID, adminDto))
                .andExpect(status().isOk())
                .andExpect(view().name("board/free/free-view"));
    }

    @Test
    @DisplayName("자유게시판 삭제 - /board/free로 리다이렉트")
    void deleteBoard_returnsRedirectToList() throws Exception {
        // given
        FreeBoardDto board = FreeBoardDto.builder().boardId(1L).title("테스트").build();
        when(freeBoardService.getBoardById(1L)).thenReturn(Optional.of(board));
        doNothing().when(freeBoardService).deleteBoard(1L);

        // when & then
        mockMvc.perform(get("/board/free/delete/1")
                        .sessionAttr(LoginController.ADMIN_SESSION_ID, adminDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/board/free*"));
    }
}

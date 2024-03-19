package com.admin.backend.controller;

import com.admin.backend.common.exception.BoardNotFoundException;
import com.admin.backend.common.type.Author;
import com.admin.backend.common.type.Board;
import com.admin.backend.common.utils.PaginationUtils;
import com.admin.backend.common.validator.BoardValidator;
import com.admin.backend.common.validator.SearchConditionValidator;
import com.admin.backend.dto.*;
import com.admin.backend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Gallery Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class GalleryController {

    private final GalleryBoardService galleryBoardService;
    private final CategoryService categoryService;
    private final FileService fileService;
    private final UploadService uploadService;
    private final CommentService commentService;


    /**
     * 갤러리 게시판 리스트 페이지 GET
     *
     * @param model              Model
     * @param adminDto           저장된 세션 정보 ( nav에 필요한 정보 )
     * @param searchConditionDto 게시물 검색 조건
     * @return /board/gallery/free-list.html
     */
    @GetMapping("/board/gallery")
    public String getListPage(Model model,
                              @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                              @ModelAttribute SearchConditionDto searchConditionDto) {

        // 검색조건 유효성 검증
        SearchConditionValidator.validateSearchCondition(searchConditionDto);

        // 페이지네이션 설정
        int totalRowCount = galleryBoardService.getTotalRowCountByCondition(searchConditionDto);
        int totalPageNum = PaginationUtils.getTotalPageNum(totalRowCount, searchConditionDto.getPageSize());

        // 데이터 가져오기
        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(Board.GALLERY_BOARD.getBoardType());
        List<GalleryBoardDto> galleryBoardDtoList = galleryBoardService.getBoardListByCondition(searchConditionDto);

        model.addAttribute("categoryList", categoryDtoList);
        model.addAttribute("boardList", galleryBoardDtoList);
        model.addAttribute("totalPageNum", totalPageNum);
        model.addAttribute("searchCondition", searchConditionDto);
        model.addAttribute("admin", adminDto);

        return "/board/gallery/gallery-list";
    }

    /**
     * 갤러리 게시판 쓰기 페이지 GET
     *
     * @param model              Model
     * @param adminDto           저장된 세션 정보 ( nav에 필요한 정보 )
     * @param searchConditionDto 게시물 검색 조건 (검색 조건을 유지하기 위한 데이터)
     * @return /board/gallery/gallery-write.html
     */
    @GetMapping("/board/gallery/write")
    public String getWritePage(Model model,
                               @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                               @ModelAttribute SearchConditionDto searchConditionDto) {

        // 카테고리 리스트 가져오기
        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(Board.GALLERY_BOARD.getBoardType());

        model.addAttribute("categoryList", categoryDtoList);
        model.addAttribute("searchCondition", searchConditionDto);
        model.addAttribute("admin", adminDto);

        return "/board/gallery/gallery-write";
    }

    /**
     * 갤러리 게시판 추가 POST
     *
     * @param adminDto        저장된 세션 정보 ( table column에 저장될 데이터 )
     * @param galleryBoardDto gallery-write form data ( categoryId , title, content, fileList )
     * @return redirect:/admin/board/gallery
     * @throws IOException IOException
     */
    @PostMapping("/board/gallery/write")
    public String addBoard(@SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                           @ModelAttribute GalleryBoardDto galleryBoardDto) throws IOException {

        // 유효성 검증
        BoardValidator.validateGalleryBoard(galleryBoardDto);

        // author 세팅
        galleryBoardDto.setAuthorId(adminDto.getAdminId());
        galleryBoardDto.setAuthorType(Author.ADMIN.getAuthorType());

        // free board 추가 후 생성 된 pk return
        Long boardId = galleryBoardService.addBoard(galleryBoardDto);

        // file db 추가 후 upload
        if (galleryBoardDto.getFile() != null) {
            List<FileDto> fileDtoList = fileService.addFile(galleryBoardDto.getFile(), Board.GALLERY_BOARD.getBoardType(), boardId);
            uploadService.uploadFile(fileDtoList, galleryBoardDto.getFile(), Board.GALLERY_BOARD.getBoardType());
        }

        return "redirect:/admin/board/gallery";
    }

    /**
     * 갤러리 게시판 보기 페이지 GET
     *
     * @param model              Model
     * @param boardId            PathVariable
     * @param adminDto           저장된 세션 정보 ( nav에 필요한 정보 )
     * @param searchConditionDto 게시물 검색 조건 (검색 조건을 유지하기 위한 데이터)
     * @return /board/gallery/gallery-view
     */
    @GetMapping("/board/gallery/{boardId}")
    public String getBoardPage(Model model,
                               @PathVariable(name = "boardId") Long boardId,
                               @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                               @ModelAttribute SearchConditionDto searchConditionDto) {

        // 데이터 가져오기
        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(Board.GALLERY_BOARD.getBoardType());
        GalleryBoardDto galleryBoardDto = galleryBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException("잘못된 요청입니다."));
        List<FileDto> fileDtoList = fileService.getFileListByBoardId(boardId, Board.GALLERY_BOARD.getBoardType());
        List<CommentDto> commentDtoList = commentService.getCommentListByBoardId(boardId, Board.GALLERY_BOARD.getBoardType());
        int fileCountInBoard = fileService.getRowCountByBoardId(boardId, Board.GALLERY_BOARD.getBoardType());

        // 조회수 증가
        galleryBoardService.increaseView(boardId);

        model.addAttribute("admin", adminDto);
        model.addAttribute("categoryList", categoryDtoList);
        model.addAttribute("searchCondition", searchConditionDto);
        model.addAttribute("board", galleryBoardDto);
        model.addAttribute("fileList", fileDtoList);
        model.addAttribute("commentList", commentDtoList);
        model.addAttribute("fileCountInBoard", fileCountInBoard);

        return "/board/gallery/gallery-view";
    }

    /**
     * 갤러리 게시판 수정 POST
     *
     * @param boardId PathVariable
     * @param galleryBoardDto gallery-modify form data ( categoryId , title, content, fileList, deletedFileIdList )
     * @return
     * @throws IOException
     */
    @PostMapping("/board/gallery/modify/{boardId}")
    public String modifyBoard(@PathVariable(name = "boardId") Long boardId,
                              @ModelAttribute GalleryBoardDto galleryBoardDto) throws IOException {

        // 유효성 검증에 필요한 현재 파일 개수
        int fileCountInBoard = fileService.getRowCountByBoardId(boardId, Board.GALLERY_BOARD.getBoardType());

        // 유효성 검사
        BoardValidator.validateGalleryBoard(galleryBoardDto ,fileCountInBoard);

        // boardId 세팅
        galleryBoardDto.setBoardId(boardId);

        // 수정 및 삭제
        galleryBoardService.modifyBoard(galleryBoardDto);
        if(galleryBoardDto.getDeleteFileIdList() != null) {
            fileService.deleteFileList(galleryBoardDto.getDeleteFileIdList());
        }

        // file db 추가 후 upload
        if (galleryBoardDto.getFile() != null) {
            List<FileDto> fileDtoList = fileService.addFile(galleryBoardDto.getFile(), Board.GALLERY_BOARD.getBoardType(), boardId);
            uploadService.uploadFile(fileDtoList, galleryBoardDto.getFile(), Board.GALLERY_BOARD.getBoardType());
        }

        return "redirect:/admin/board/gallery";
    }

    /**
     * 갤러리 게시판 삭제
     *
     * @param boardId PathVariable
     * @return redirect:/admin/board/gallery
     */
    @GetMapping("/board/gallery/delete/{boardId}")
    public String deleteBoard(@PathVariable(name = "boardId") Long boardId) {

        // TODO : findByID로 검사?

        // 삭제
        galleryBoardService.deleteBoard(boardId);

        return "redirect:/admin/board/gallery";
    }

}

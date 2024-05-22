package com.admin.backend.controller;

import com.admin.backend.common.exception.BoardNotFoundException;
import com.admin.backend.common.exception.IllegalFileDataException;
import com.admin.backend.common.type.Author;
import com.admin.backend.common.type.Board;
import com.admin.backend.common.utils.BindingResultUtils;
import com.admin.backend.common.utils.MultipartFileUtils;
import com.admin.backend.common.utils.PaginationUtils;
import com.admin.backend.common.utils.StringUtils;
import com.admin.backend.common.validator.FileValidator;
import com.admin.backend.common.validator.constraint.GalleryBoardFileConstraint;
import com.admin.backend.dto.*;
import com.admin.backend.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

/**
 * Gallery Controller
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class GalleryBoardController {

    private final GalleryBoardService galleryBoardService;
    private final CategoryService categoryService;
    private final FileService fileService;
    private final CommentService commentService;
    private final FileStorageService fileStorageService;
    private final FileValidator<GalleryBoardFileConstraint> fileValidator;


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

        return "board/gallery/gallery-list";
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

        return "board/gallery/gallery-write";
    }


    /**
     * 갤러리 게시판 추가 POST
     *
     * @param adminDto        저장된 세션 정보 ( table column에 저장될 데이터 )
     * @param galleryBoardDto gallery-write form data ( categoryId , title, content )
     * @param fileList        등록될 파일
     * @return redirect:/board/gallery
     * @throws IOException IOException
     */
    @PostMapping("/board/gallery/write")
    public String addBoard(@SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                           @Valid @ModelAttribute GalleryBoardDto galleryBoardDto,
                           BindingResult bindingResult,
                           @ModelAttribute SearchConditionDto searchConditionDto,
                           @RequestParam(name = "file") MultipartFile[] fileList,
                           RedirectAttributes redirectAttributes) {

        // 텍스트 검증
        if (bindingResult.hasErrors()) {
            String errorMessage = BindingResultUtils.getErrorMessage(bindingResult, new String[]{"title", "content"});
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/board/gallery/write" + StringUtils.searchConditionToQueryStringWithCategory(searchConditionDto);
        }

        // 파일 리스트 내부 null 삭제 및 유효성 검증
        fileList = MultipartFileUtils.replaceEmptyFile(fileList);
        try {
            fileValidator.validateFile(fileList);
        } catch (IllegalFileDataException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/board/gallery/write" + StringUtils.searchConditionToQueryStringWithCategory(searchConditionDto);
        }

        // Author set
        galleryBoardDto.setAuthorId(adminDto.getAdminId());
        galleryBoardDto.setAuthorType(Author.ADMIN.getAuthorType());

        Long boardId = galleryBoardService.addBoard(galleryBoardDto);

        // 파일 저장
        if (fileList != null) {
            fileStorageService.storageFileList(fileList, boardId, Board.GALLERY_BOARD.getBoardType(), true);
        }

        // 등록 완료 응답 값 설정
        redirectAttributes.addFlashAttribute("write", 1);

        return "redirect:/board/gallery";
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
        GalleryBoardDto galleryBoardDto = galleryBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException("잘못된 요청입니다."));
        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(Board.GALLERY_BOARD.getBoardType());
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

        return "board/gallery/gallery-view";
    }

    /**
     * 갤러리 게시판 수정 POST
     *
     * @param boardId            PathVariable
     * @param galleryBoardDto    gallery-modify form data ( categoryId , title, content )
     * @param fileList           등록 될 파일
     * @param searchConditionDto 수정 후 검색조건을 유지하기 위한 쿼리스트링
     * @param redirectAttributes 수정 완료 alert를 위한 redirectAttributes
     */
    @PostMapping("/board/gallery/modify/{boardId}")
    public String modifyBoard(@PathVariable(name = "boardId") Long boardId,
                              @Valid @ModelAttribute GalleryBoardDto galleryBoardDto,
                              BindingResult bindingResult,
                              @RequestParam(name = "file", required = false) MultipartFile[] fileList,
                              @RequestParam(name = "deleteFileIdList", required = false) List<Long> deleteFileIdList,
                              @ModelAttribute SearchConditionDto searchConditionDto,
                              RedirectAttributes redirectAttributes) {

        // boardId 유효성 검증
        galleryBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException("잘못된 요청입니다."));

        // 텍스트 검증
        if (bindingResult.hasErrors()) {
            String errorMessage = BindingResultUtils.getErrorMessage(bindingResult, new String[]{"title", "content"});
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/board/gallery/" + boardId + StringUtils.searchConditionToQueryStringWithCategory(searchConditionDto);
        }

        // 파일 리스트 내부 null 삭제 및 유효성 검증
        fileList = MultipartFileUtils.replaceEmptyFile(fileList);
        try {
            // 유효성 검증에 필요한 현재 파일 개수
            int currentFileCount = fileService.getRowCountByBoardId(boardId, Board.GALLERY_BOARD.getBoardType());
            fileValidator.validateFileForModify(fileList, deleteFileIdList, currentFileCount);
        } catch (IllegalFileDataException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/board/gallery/" + boardId + StringUtils.searchConditionToQueryStringWithCategory(searchConditionDto);
        }

        // boardId 세팅
        galleryBoardDto.setBoardId(boardId);

        boolean isThumbnailDeleted = false;

        // 파일 삭제
        if (deleteFileIdList != null) {
            // 썸네일 이미지 삭제인지
            isThumbnailDeleted = fileStorageService.deleteFileList(deleteFileIdList);
        }


        // 파일 추가
        if (fileList != null) {
            // 파일 추가
            fileStorageService.storageFileList(fileList, boardId, Board.GALLERY_BOARD.getBoardType(), isThumbnailDeleted);
        } else if (isThumbnailDeleted) {
            // 추가 없이 썸네일 파일 삭제 포함 있을 경우
            FileDto fileDto = fileService.getFileListByBoardId(boardId, Board.GALLERY_BOARD.getBoardType()).get(0);
            fileStorageService.storageThumbnail(fileDto);
        }

        // 수정
        galleryBoardService.modifyBoard(galleryBoardDto);

        // 수정 완료 응답 값 설정
        redirectAttributes.addFlashAttribute("modify", 1);

        return "redirect:/board/gallery/" + boardId + StringUtils.searchConditionToQueryStringWithCategory(searchConditionDto);
    }

    /**
     * 갤러리 게시판 삭제
     *
     * @param boardId            PathVariable
     * @param searchConditionDto 삭제 완료 후 검색 조건 유지를 위한 쿼리스트링
     * @param redirectAttributes 삭제 완료 alert를 위한 redirectAttributes
     * @return redirect:/board/gallery
     */
    @GetMapping("/board/gallery/delete/{boardId}")
    public String deleteBoard(@PathVariable(name = "boardId") Long boardId,
                              @ModelAttribute SearchConditionDto searchConditionDto,
                              RedirectAttributes redirectAttributes) {

        // boardId 유효성 검증
        galleryBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException("잘못된 요청입니다."));

        // 삭제
        galleryBoardService.deleteBoard(boardId);

        // 삭제 완료 응답 값 설정
        redirectAttributes.addFlashAttribute("delete", 1);

        return "redirect:/board/gallery" + StringUtils.searchConditionToQueryStringWithCategory(searchConditionDto);
    }

}

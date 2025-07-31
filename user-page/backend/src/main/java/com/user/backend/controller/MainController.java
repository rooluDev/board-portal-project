package com.user.backend.controller;

import com.user.backend.dto.*;
import com.user.backend.service.FreeBoardService;
import com.user.backend.service.GalleryBoardService;
import com.user.backend.service.InquiryBoardService;
import com.user.backend.service.NoticeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main Controller
 */
@RestController
@RequestMapping("/api")
public class MainController {

    private final NoticeBoardService noticeBoardService;
    private final GalleryBoardService galleryBoardService;
    private final FreeBoardService freeBoardService;
    private final InquiryBoardService inquiryBoardService;

    public MainController(@Qualifier("noticeBoardJpa") NoticeBoardService noticeBoardService,
                          @Qualifier("galleryBoardJpa") GalleryBoardService galleryBoardService,
                          @Qualifier("freeBoardJpa") FreeBoardService freeBoardService,
                          @Qualifier("inquiryBoardJpa") InquiryBoardService inquiryBoardService) {
        this.noticeBoardService = noticeBoardService;
        this.galleryBoardService = galleryBoardService;
        this.freeBoardService = freeBoardService;
        this.inquiryBoardService = inquiryBoardService;
    }

    /**
     * 공지사항, 자유게시판, 문의게시판 최근 6개, 갤러리게시판 최근 3개의 데이터 GET
     *
     * @return {
     * freeBoardList : [],
     * noticeBoardList : [],
     * galleryBoardList : [],
     * inquiryBoardList : []
     * }
     */
    @GetMapping("/boards/all")
    public ResponseEntity<Map> getBoardListForMain() {

        // 데이터 가져오기
        List<NoticeBoardDto> noticeBoardDtoList = noticeBoardService.getBoardListForMain();
        List<GalleryBoardDto> galleryBoardDtoList = galleryBoardService.getBoardListForMain();
        List<FreeBoardDto> freeBoardDtoList = freeBoardService.getBoardListForMain();
        List<InquiryBoardDto> inquiryBoardDtoList = inquiryBoardService.getBoardListForMain();

        Map response = new HashMap();
        response.put("noticeBoardList", noticeBoardDtoList);
        response.put("galleryBoardList", galleryBoardDtoList);
        response.put("freeBoardList", freeBoardDtoList);
        response.put("inquiryBoardList", inquiryBoardDtoList);

        return ResponseEntity.ok().body(response);
    }
}

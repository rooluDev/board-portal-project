package com.admin.backend.controller;

import com.admin.backend.service.FileService;
import com.admin.backend.service.GalleryBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Gallery Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping
public class GalleryController {

    private final GalleryBoardService galleryBoardService;
    private final FileService fileService;
}

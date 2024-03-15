package com.admin.backend.service;

import com.admin.backend.mapper.GalleryBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Gallery Board Service 구현체
 */
@Service
@Primary
@RequiredArgsConstructor
public class GalleryBoardServiceImpl implements GalleryBoardService{

    private final GalleryBoardMapper galleryBoardMapper;
}

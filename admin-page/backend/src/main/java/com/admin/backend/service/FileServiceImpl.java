package com.admin.backend.service;

import com.admin.backend.common.utils.MultipartFileUtils;
import com.admin.backend.dto.FileDto;
import com.admin.backend.mapper.FileMapper;
import com.admin.backend.mapper.ThumbnailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * File Service 구현체
 */
@Primary
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

    @Override
    public Long addFileList(List<FileDto> fileList, Long boardId) {
        List<Long> pk = new ArrayList<>();
        for (FileDto fileDto : fileList) {
            fileDto.setBoardId(boardId);
            fileMapper.insertFile(fileDto);
            pk.add(fileDto.getFileId());
        }
        // return 첫번째 ID
        return pk.get(0);
    }

    @Override
    public List<FileDto> getFileListByBoardId(Long boardId, String boardType) {
        return fileMapper.selectFileListByBoardId(boardId, boardType);
    }

    @Override
    public Optional<FileDto> getFileById(Long fileId) {
        return fileMapper.selectFileById(fileId);
    }

    @Override
    public void deleteFileList(List<Long> deleteFileIdList) {
        for(Long fileId : deleteFileIdList){
            fileMapper.deleteFileById(fileId);
        }
    }

    @Override
    public int getRowCountByBoardId(Long boardId, String boardType) {
        return fileMapper.selectRowCountByBoardId(boardId, boardType);
    }
}

package com.user.backend.service.jpa;

import com.user.backend.dto.FileDto;
import com.user.backend.entity.File;
import com.user.backend.repository.FileRepository;
import com.user.backend.service.FileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * File Service Jpa Impl
 */
@Service("fileJpa")
@RequiredArgsConstructor
@Transactional
public class FileServiceJpaImpl implements FileService {

    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<FileDto> addFileList(List<FileDto> fileList, Long boardId) {
        List<File> fileEntityList = fileList.stream()
                .map(dto -> {
                    dto.setBoardId(boardId);
                    return modelMapper.map(dto, File.class);
                })
                .toList();

        List<File> savedFileEntityList = fileRepository.saveAll(fileEntityList);

        return savedFileEntityList.stream()
                .map(file -> modelMapper.map(file, FileDto.class))
                .toList();
    }

    @Override
    public List<FileDto> getFileListByBoardId(Long boardId, String boardType) {
        return fileRepository.findByBoardTypeAndBoardId(boardType, boardId)
                .stream()
                .map(entity -> modelMapper.map(entity, FileDto.class))
                .toList();
    }

    @Override
    public Optional<FileDto> getFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .map(entity -> modelMapper.map(entity, FileDto.class));
    }

    @Override
    @Transactional
    public void deleteFileList(List<Long> deleteFileIdList) {
        fileRepository.deleteAllById(deleteFileIdList);

    }

    @Override
    public int getFileCountByBoardId(Long boardId, String boardType) {
        return (int) fileRepository.countByBoardTypeAndBoardId(boardType, boardId);
    }
}

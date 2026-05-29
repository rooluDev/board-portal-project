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

    /**
     * 파일 리스트를 DB에 등록하고 저장된 FileDto 리스트 반환
     *
     * @param fileList 저장할 파일 Dto 리스트
     * @param boardId  파일이 속한 게시물 ID
     * @return DB에 저장된 파일 Dto 리스트
     */
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

    /**
     * 특정 게시물에 있는 파일 리스트 가져오기
     *
     * @param boardId   게시물 ID
     * @param boardType 게시판 타입
     * @return 해당 게시물의 파일 리스트
     */
    @Override
    public List<FileDto> getFileListByBoardId(Long boardId, String boardType) {
        return fileRepository.findByBoardTypeAndBoardId(boardType, boardId)
                .stream()
                .map(entity -> modelMapper.map(entity, FileDto.class))
                .toList();
    }

    /**
     * 파일 ID로 파일 정보 가져오기
     *
     * @param fileId 파일 ID (pk)
     * @return 해당 파일 ID의 파일 Optional
     */
    @Override
    public Optional<FileDto> getFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .map(entity -> modelMapper.map(entity, FileDto.class));
    }

    /**
     * 파일 ID 리스트에 해당하는 파일들을 DB에서 삭제
     *
     * @param deleteFileIdList 삭제할 파일들의 ID 리스트
     */
    @Override
    @Transactional
    public void deleteFileList(List<Long> deleteFileIdList) {
        fileRepository.deleteAllById(deleteFileIdList);

    }

    /**
     * 특정 게시물에 속한 파일 개수 조회
     *
     * @param boardId   게시물 ID
     * @param boardType 게시판 타입
     * @return 해당 게시물의 파일 개수
     */
    @Override
    public int getFileCountByBoardId(Long boardId, String boardType) {
        return (int) fileRepository.countByBoardTypeAndBoardId(boardType, boardId);
    }
}

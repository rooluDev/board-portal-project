package com.user.backend.service.jpa;

import com.user.backend.dto.ThumbnailDto;
import com.user.backend.entity.File;
import com.user.backend.entity.Thumbnail;
import com.user.backend.repository.FileRepository;
import com.user.backend.repository.ThumbnailRepository;
import com.user.backend.service.ThumbnailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * ThumbnailServiceJpaImpl
 */
@Service("thumbnailJpa")
@RequiredArgsConstructor
@Transactional
public class ThumbnailServiceJpaImpl implements ThumbnailService {

    private final ThumbnailRepository thumbnailRepository;
    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;

    /**
     * 썸네일 DB 추가
     *
     * @param thumbnailDto 추가할 썸네일 정보
     */
    @Override
    @Transactional
    public void addThumbnail(ThumbnailDto thumbnailDto) {
        File file = fileRepository.findById(thumbnailDto.getFileId()).orElseThrow(() -> new EntityNotFoundException("Thumbnail file not found"));
        Thumbnail thumbnail = modelMapper.map(thumbnailDto, Thumbnail.class);
        thumbnail.setFile(file);
        thumbnailRepository.save(thumbnail);
    }

    /**
     * fileId에 연결된 썸네일 DB 삭제
     *
     * @param fileId 삭제할 썸네일의 파일 ID (tb_file pk)
     * @return 삭제된 row count
     */
    @Override
    @Transactional
    public int deleteThumbnailByFileId(Long fileId) {
        return thumbnailRepository.deleteByFileFileId(fileId);
    }

    /**
     * 썸네일 ID로 썸네일 정보 가져오기
     *
     * @param thumbnailId 썸네일 ID (tb_thumbnail pk)
     * @return 해당 thumbnailId의 ThumbnailDto Optional
     */
    @Override
    public Optional<ThumbnailDto> getThumbnailById(Long thumbnailId) {
        // ModelMapper 사용 시 Thumbnail.file.fileId vs Thumbnail.file.boardId 매핑 충돌 발생
        // → 수동 매핑으로 교체
        return thumbnailRepository.findById(thumbnailId)
                .map(thumbnail -> ThumbnailDto.builder()
                        .thumbnailId(thumbnail.getThumbnailId())
                        .fileId(thumbnail.getFile().getFileId())
                        .originalName(thumbnail.getOriginalName())
                        .physicalName(thumbnail.getPhysicalName())
                        .filePath(thumbnail.getFilePath())
                        .extension(thumbnail.getExtension())
                        .size(thumbnail.getSize())
                        .createdAt(thumbnail.getCreatedAt())
                        .editedAt(thumbnail.getEditedAt())
                        .build());
    }
}

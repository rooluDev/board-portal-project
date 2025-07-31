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

    @Override
    @Transactional
    public void addThumbnail(ThumbnailDto thumbnailDto) {
        File file = fileRepository.findById(thumbnailDto.getFileId()).orElseThrow(() -> new EntityNotFoundException("Thumbnail file not found"));
        Thumbnail thumbnail = modelMapper.map(thumbnailDto, Thumbnail.class);
        thumbnail.setFile(file);
        thumbnailRepository.save(thumbnail);
    }

    @Override
    @Transactional
    public int deleteThumbnailByFileId(Long fileId) {
        return thumbnailRepository.deleteByFileFileId(fileId);
    }

    @Override
    public Optional<ThumbnailDto> getThumbnailById(Long thumbnailId) {
        return thumbnailRepository.findById(thumbnailId)
                .map(thumbnail -> modelMapper.map(thumbnail, ThumbnailDto.class));
    }
}

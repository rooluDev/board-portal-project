package com.admin.backend.service;

import com.admin.backend.dto.AdminDto;
import com.admin.backend.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * AdminService 구현체
 */
@RequiredArgsConstructor
@Service
@Primary
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    @Override
    public Optional<AdminDto> findAdmin(String adminId, String password) {
        return adminMapper.selectAdminDtoByIdAndPassword(adminId, password);
    }
}

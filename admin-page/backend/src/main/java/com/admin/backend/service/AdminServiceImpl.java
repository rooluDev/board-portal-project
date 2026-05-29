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

    /**
     * ID와 password가 일치하는 Admin 찾기
     *
     * @param adminId  관리자 ID
     * @param password 관리자 비밀번호
     * @return ID와 PW가 일치하는 Admin 데이터
     */
    @Override
    public Optional<AdminDto> findAdmin(String adminId, String password) {
        return adminMapper.selectAdminDtoByIdAndPassword(adminId, password);
    }
}

package com.admin.backend.service;

import com.admin.backend.dto.AdminDto;

import java.util.Optional;

/**
 * Admin Service Interface
 */
public interface AdminService {

    /**
     * ID와 password가 일치하는 AdminDto 찾는 메소드
     *
     * @param adminId
     * @param password
     * @return
     */
    Optional<AdminDto> findAdmin(String adminId, String password);
}

package com.admin.backend.service;

import com.admin.backend.dto.AdminDto;

import java.util.Optional;

/**
 * Admin Service Interface
 */
public interface AdminService {

    /**
     * ID와 password가 일치하는 Admin 찾기
     *
     * @param adminId  ID
     * @param password PW
     * @return ID와 PW가 일치하는 Admin Data
     */
    Optional<AdminDto> findAdmin(String adminId, String password);
}

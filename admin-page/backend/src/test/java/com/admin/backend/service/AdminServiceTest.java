package com.admin.backend.service;

import com.admin.backend.dto.AdminDto;
import com.admin.backend.mapper.AdminMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminMapper adminMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    @DisplayName("관리자 로그인 성공 - 올바른 ID/PW로 관리자 반환")
    void findAdmin_withValidCredentials_returnsAdmin() {
        // given
        String adminId = "admin";
        String password = "hashedPassword";
        AdminDto expected = new AdminDto();
        expected.setAdminId(adminId);
        expected.setAdminName("관리자");
        when(adminMapper.selectAdminDtoByIdAndPassword(adminId, password)).thenReturn(Optional.of(expected));

        // when
        Optional<AdminDto> result = adminService.findAdmin(adminId, password);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getAdminId()).isEqualTo(adminId);
    }

    @Test
    @DisplayName("관리자 로그인 실패 - 잘못된 ID/PW는 빈 Optional 반환")
    void findAdmin_withInvalidCredentials_returnsEmpty() {
        // given
        String adminId = "admin";
        String wrongPassword = "wrongPassword";
        when(adminMapper.selectAdminDtoByIdAndPassword(adminId, wrongPassword)).thenReturn(Optional.empty());

        // when
        Optional<AdminDto> result = adminService.findAdmin(adminId, wrongPassword);

        // then
        assertThat(result).isEmpty();
    }
}

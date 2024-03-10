package com.admin.backend.mapper;

import com.admin.backend.dto.AdminDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * tb_Admin DB Mapper
 */
@Mapper
public interface AdminMapper {
    /**
     * ID와 password가 일치하는 tb_admin SELECT
     * @param adminId
     * @param password
     * @return
     */
    Optional<AdminDto> selectAdminDtoByIdAndPassword(@Param("adminId") String adminId, @Param("password") String password);
}

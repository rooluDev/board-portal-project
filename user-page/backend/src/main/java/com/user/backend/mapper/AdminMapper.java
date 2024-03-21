package com.user.backend.mapper;

import com.user.backend.dto.AdminDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * tb_admin Mapper
 */
@Mapper
public interface AdminMapper {

    /**
     * SELECT tb_admin By ID and password
     *
     * @param adminId  ID
     * @param password PW
     * @return ID와 PW가 일치하는 admin data
     */
    Optional<AdminDto> selectAdminDtoByIdAndPassword(@Param("adminId") String adminId, @Param("password") String password);
}

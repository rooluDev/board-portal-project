package com.user.backend.mapper;

import com.user.backend.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * Member Mapper
 */
@Mapper
public interface MemberMapper {

    /**
     * SELECT tb_member By ID and password
     *
     * @param memberId
     * @param password
     * @return
     */
    Optional<MemberDto> selectMemberByIdAndPassword(@Param("memberId") String memberId, @Param("password") String password);

    /**
     * SELECT tb_member By memberId
     *
     * @param memberId memberId
     * @return memberId와 일치하는 member
     */
    Optional<MemberDto> selectMemberById(String memberId);

    /**
     * INSERT tb_member
     *
     * @param memberDto ( member_id, member_name, password )
     */
    void insertMember(MemberDto memberDto);
}

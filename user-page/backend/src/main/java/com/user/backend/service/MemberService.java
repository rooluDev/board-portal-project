package com.user.backend.service;

import com.user.backend.dto.MemberDto;

import java.util.Optional;

/**
 * Member Service Interface
 */
public interface MemberService {

    /**
     * memberId로 member 데이터 가져오기
     *
     * @param memberId memberId
     * @return member
     */
    Optional<MemberDto> findById(String memberId);

    /**
     * member 추가
     *
     * @param memberDto ( member_id, member_name, password )
     */
    void addMember(MemberDto memberDto);
}

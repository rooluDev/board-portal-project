package com.user.backend.service.mybatis;

import com.user.backend.dto.MemberDto;
import com.user.backend.mapper.MemberMapper;
import com.user.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * MemberService Impl
 */
@Service("memberMybatis")
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;

    /**
     * memberId로 회원 정보 가져오기
     *
     * @param memberId 회원 ID
     * @return 해당 회원 ID의 MemberDto Optional
     */
    @Override
    public Optional<MemberDto> findById(String memberId) {
        return memberMapper.selectMemberById(memberId);
    }

    /**
     * 회원 추가
     *
     * @param memberDto 추가할 회원 정보 (memberId, memberName, password)
     */
    @Override
    public void addMember(MemberDto memberDto) {
        memberMapper.insertMember(memberDto);
    }
}

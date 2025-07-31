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

    @Override
    public Optional<MemberDto> findById(String memberId) {
        return memberMapper.selectMemberById(memberId);
    }

    @Override
    public void addMember(MemberDto memberDto) {
        memberMapper.insertMember(memberDto);
    }
}

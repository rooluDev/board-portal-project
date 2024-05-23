package com.user.backend.service;

import com.user.backend.dto.MemberDto;
import com.user.backend.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * MemberService Impl
 */
@Service
@Primary
@RequiredArgsConstructor
@Slf4j
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

package com.user.backend.service;

import com.user.backend.dto.MemberDto;
import com.user.backend.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Login Service Impl
 */
@Service
@Primary
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private final MemberMapper memberMapper;

    @Override
    public Optional<MemberDto> login(String memberId, String password) {

        return memberMapper.selectMemberByIdAndPassword(memberId, password);
    }
}

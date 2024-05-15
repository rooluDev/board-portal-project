package com.user.backend.service;

import com.user.backend.common.exception.custom.MemberNotFoundException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.dto.MemberDto;
import com.user.backend.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


@Service
@Primary
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private final MemberMapper memberMapper;

    @Override
    public MemberDto login(String memberId, String password) {

        return memberMapper.selectMemberByIdAndPassword(memberId, password).orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
}

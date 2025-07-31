package com.user.backend.service.mybatis;

import com.user.backend.dto.MemberDto;
import com.user.backend.mapper.MemberMapper;
import com.user.backend.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Login Service Impl
 */
@Service("loginMybatis")
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final MemberMapper memberMapper;

    @Override
    public Optional<MemberDto> login(String memberId, String password) {

        return memberMapper.selectMemberByIdAndPassword(memberId, password);
    }
}

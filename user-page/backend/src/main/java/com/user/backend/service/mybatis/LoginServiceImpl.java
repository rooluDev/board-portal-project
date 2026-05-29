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

    /**
     * 로그인 진행 후 Member 데이터 반환
     *
     * @param memberId 회원 ID
     * @param password 비밀번호
     * @return ID와 PW가 일치하는 MemberDto Optional
     */
    @Override
    public Optional<MemberDto> login(String memberId, String password) {

        return memberMapper.selectMemberByIdAndPassword(memberId, password);
    }
}

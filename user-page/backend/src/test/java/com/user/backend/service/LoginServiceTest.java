package com.user.backend.service;

import com.user.backend.dto.MemberDto;
import com.user.backend.mapper.MemberMapper;
import com.user.backend.service.mybatis.LoginServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private LoginServiceImpl loginService;

    @Test
    @DisplayName("로그인 성공 - 올바른 ID/PW로 회원 반환")
    void login_withValidCredentials_returnsMember() {
        // given
        String memberId = "user1";
        String password = "hashedPassword";
        MemberDto expected = new MemberDto();
        expected.setMemberId(memberId);
        expected.setMemberName("홍길동");
        when(memberMapper.selectMemberByIdAndPassword(memberId, password)).thenReturn(Optional.of(expected));

        // when
        Optional<MemberDto> result = loginService.login(memberId, password);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getMemberId()).isEqualTo(memberId);
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 ID/PW는 빈 Optional 반환")
    void login_withInvalidCredentials_returnsEmpty() {
        // given
        String memberId = "user1";
        String wrongPassword = "wrongPassword";
        when(memberMapper.selectMemberByIdAndPassword(memberId, wrongPassword)).thenReturn(Optional.empty());

        // when
        Optional<MemberDto> result = loginService.login(memberId, wrongPassword);

        // then
        assertThat(result).isEmpty();
    }
}

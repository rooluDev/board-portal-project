package com.user.backend.service;

import com.user.backend.dto.MemberDto;
import com.user.backend.mapper.MemberMapper;
import com.user.backend.service.mybatis.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("회원 ID 조회 - 존재하는 회원 반환")
    void findById_whenExists_returnsMember() {
        // given
        String memberId = "user1";
        MemberDto expected = new MemberDto();
        expected.setMemberId(memberId);
        expected.setMemberName("홍길동");
        when(memberMapper.selectMemberById(memberId)).thenReturn(Optional.of(expected));

        // when
        Optional<MemberDto> result = memberService.findById(memberId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getMemberName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("회원 ID 조회 - 존재하지 않는 회원은 빈 Optional 반환")
    void findById_whenNotExists_returnsEmpty() {
        // given
        String memberId = "noUser";
        when(memberMapper.selectMemberById(memberId)).thenReturn(Optional.empty());

        // when
        Optional<MemberDto> result = memberService.findById(memberId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("회원 가입 - mapper insert 호출")
    void addMember_callsInsert() {
        // given
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId("newUser");
        memberDto.setMemberName("김철수");
        memberDto.setPassword("password123");
        doNothing().when(memberMapper).insertMember(memberDto);

        // when
        memberService.addMember(memberDto);

        // then
        verify(memberMapper, times(1)).insertMember(memberDto);
    }
}

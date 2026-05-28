package com.user.backend.service;

import com.user.backend.dto.MemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MemberService 통합 테스트
 * - 실제 MySQL DB 연결 필요 (dev 프로파일)
 */
@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class MemberServiceIntegrationTest {

    @Autowired
    @Qualifier("memberJpa")
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입 후 ID 조회 - 저장된 회원 반환")
    void addMember_thenFindById_returnsMember() {
        // given
        MemberDto newMember = new MemberDto();
        newMember.setMemberId("testid123");
        newMember.setMemberName("테스트유저");
        newMember.setPassword("testpass1");

        // when
        memberService.addMember(newMember);
        Optional<MemberDto> result = memberService.findById("testid123");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getMemberName()).isEqualTo("테스트유저");
    }

    @Test
    @DisplayName("존재하지 않는 ID 조회 - 빈 Optional 반환")
    void findById_whenNotExists_returnsEmpty() {
        // when
        Optional<MemberDto> result = memberService.findById("nonexistent");

        // then
        assertThat(result).isEmpty();
    }
}

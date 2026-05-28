package com.user.backend.repository;

import com.user.backend.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MemberRepository 단위 테스트
 * - 실제 MySQL DB 연결 필요 (dev 프로파일)
 * - findByMemberIdAndPassword 는 MySQL의 SHA2() 함수를 사용하는 네이티브 쿼리
 * - @Transactional 로 각 테스트 후 롤백
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // SHA2('testpass1', 256) 해시값으로 패스워드 저장 (MySQL 네이티브 쿼리 테스트용)
        jdbcTemplate.execute(
                "INSERT INTO tb_member (member_id, member_name, password, created_at) " +
                "VALUES ('testmem01', '테스트유저', SHA2('testpass1', 256), NOW())"
        );
    }

    @Test
    @DisplayName("ID 조회 - 존재하는 회원 반환")
    void findById_whenExists_returnsMember() {
        // when
        Optional<Member> result = memberRepository.findById("testmem01");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getMemberName()).isEqualTo("테스트유저");
    }

    @Test
    @DisplayName("ID 조회 - 존재하지 않는 회원은 빈 Optional 반환")
    void findById_whenNotExists_returnsEmpty() {
        // when
        Optional<Member> result = memberRepository.findById("nosuchuser");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("SHA2 네이티브 쿼리 - 올바른 패스워드로 조회 성공")
    void findByMemberIdAndPassword_withValidPassword_returnsMember() {
        // when
        Optional<Member> result = memberRepository.findByMemberIdAndPassword("testmem01", "testpass1");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getMemberId()).isEqualTo("testmem01");
    }

    @Test
    @DisplayName("SHA2 네이티브 쿼리 - 잘못된 패스워드는 빈 Optional 반환")
    void findByMemberIdAndPassword_withWrongPassword_returnsEmpty() {
        // when
        Optional<Member> result = memberRepository.findByMemberIdAndPassword("testmem01", "wrongpass");

        // then
        assertThat(result).isEmpty();
    }
}

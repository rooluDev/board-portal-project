package com.user.backend.repository;

import com.user.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Member Repository
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    /**
     * 로그인 처리: memberId + password(SHA-256 해시)로 조회
     */
    @Query(value = "SELECT * FROM tb_member WHERE member_id = :memberId AND password = SHA2(:password,256)",
            nativeQuery = true)
    Optional<Member> findByMemberIdAndPassword(
            @Param("memberId") String memberId,
            @Param("password") String rawPassword
    );
}

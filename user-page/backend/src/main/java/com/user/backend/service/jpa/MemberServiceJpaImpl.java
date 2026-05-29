package com.user.backend.service.jpa;

import com.user.backend.dto.MemberDto;
import com.user.backend.entity.Member;
import com.user.backend.repository.MemberRepository;
import com.user.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * MemberServiceJpaImpl
 */
@Service("memberJpa")
@RequiredArgsConstructor
@Transactional
public class MemberServiceJpaImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    /**
     * memberId로 회원 정보 가져오기
     *
     * @param memberId 회원 ID
     * @return 해당 회원 ID의 MemberDto Optional
     */
    @Override
    public Optional<MemberDto> findById(String memberId) {
        return memberRepository.findById(memberId)
                .map(member -> modelMapper.map(member, MemberDto.class));
    }

    /**
     * 회원 추가 (비밀번호를 MD5 해시로 변환 후 저장)
     *
     * @param memberDto 추가할 회원 정보 (memberId, memberName, password)
     */
    @Override
    @Transactional
    public void addMember(MemberDto memberDto) {
        String raw = memberDto.getPassword();
        String hashed = org.springframework.util.DigestUtils
                .appendMd5DigestAsHex(raw.getBytes(), new StringBuilder())
                .toString();

        Member member = modelMapper.map(memberDto, Member.class);
        member.setPassword(hashed);
        memberRepository.save(member);
    }
}

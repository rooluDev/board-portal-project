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

    @Override
    public Optional<MemberDto> findById(String memberId) {
        return memberRepository.findById(memberId)
                .map(member -> modelMapper.map(member, MemberDto.class));
    }

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

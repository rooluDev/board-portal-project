package com.user.backend.service.jpa;

import com.user.backend.dto.MemberDto;
import com.user.backend.repository.MemberRepository;
import com.user.backend.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * LoginServiceJpaImpl
 */
@Service("loginJpa")
@RequiredArgsConstructor
@Transactional
public class LoginServiceJpaImpl implements LoginService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @Override
    public Optional<MemberDto> login(String memberId, String password) {

        return memberRepository.findByMemberIdAndPassword(memberId,password)
                .map((memberDto) -> modelMapper.map(memberDto, MemberDto.class));
    }
}

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

    /**
     * 로그인 진행 후 Member 데이터 반환
     *
     * @param memberId 회원 ID
     * @param password 비밀번호
     * @return ID와 PW가 일치하는 MemberDto Optional
     */
    @Override
    public Optional<MemberDto> login(String memberId, String password) {

        return memberRepository.findByMemberIdAndPassword(memberId,password)
                .map((memberDto) -> modelMapper.map(memberDto, MemberDto.class));
    }
}

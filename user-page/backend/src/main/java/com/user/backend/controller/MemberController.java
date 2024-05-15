package com.user.backend.controller;


import com.user.backend.common.exception.custom.MemberIdExistedException;
import com.user.backend.common.exception.custom.MemberNotFoundException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.dto.MemberDto;
import com.user.backend.service.JwtService;
import com.user.backend.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Member Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;

    /**
     * JWT와 일치하는 Member 가져오기
     *
     * @param request HttpServletRequest
     * @return memberDto
     */
    @GetMapping("/member")
    public ResponseEntity getMember(HttpServletRequest request) {

        String memberId = jwtService.getMemberIdFromToken(request);

        MemberDto memberDto = memberService.findById(memberId).orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        return ResponseEntity.ok(memberDto);
    }

    /**
     * 멤버 ID 중복 검사
     *
     * @param memberId
     * @return null
     */
    @GetMapping("/member/check-duplicate")
    public ResponseEntity checkMemberId(@RequestParam(name = "memberId") String memberId) {

        memberService.findById(memberId).ifPresent(member -> {
            throw new MemberIdExistedException(ErrorCode.ID_DUPLICATE);
        });

        return ResponseEntity.ok().build();
    }

    /**
     * 회원가입
     *
     * @param memberDto 회원가입 할 정보
     * @return null
     */
    @PostMapping("/member")
    public ResponseEntity join(@Valid @RequestBody MemberDto memberDto) {

        memberService.addMember(memberDto);

        return ResponseEntity.ok().build();
    }
}

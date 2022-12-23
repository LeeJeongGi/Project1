package com.project.api.service.impl;

import com.project.api.common.exception.NotFoundException;
import com.project.api.config.security.JwtTokenProvider;
import com.project.api.dto.request.LoginForm;
import com.project.api.dto.request.MemberForm;
import com.project.api.dto.response.MemberInfo;
import com.project.api.dto.response.TokenInfo;
import com.project.api.entity.Member;
import com.project.api.repository.MemberRepository;
import com.project.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final String PRE_REGISTER_MEMBER = "이미 회원 가입한 아이디 입니다.";
    private final String NOT_FOUND_MEMBER = "존재하지 않는 유저 입니다.";
    private final String WRONG_PASSWORD = "잘못된 비밀번호입니다.";
    private final int ZERO = 0;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String saveMember(MemberForm request) {

        Optional<Member> findMember = memberRepository.findByUserId(request.getUserId());
        if (!findMember.isEmpty()) {
            throw new IllegalArgumentException(PRE_REGISTER_MEMBER);
        }

        Member member = Member.builder()
                .userId(request.getUserId().toLowerCase())
                .pw(passwordEncoder.encode(request.getPw()))
                .userName(request.getUserName())
                .roles(Collections.singletonList("ROLE_USER"))
                .points(ZERO)
                .build();

        return memberRepository.save(member).getUserId();
    }

    @Override
    public TokenInfo login(LoginForm loginForm) {

        Member member = memberRepository.findByUserId(loginForm.getUserId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));

        if (!passwordEncoder.matches(loginForm.getPw(), member.getPw())) {
            throw new IllegalArgumentException(WRONG_PASSWORD);
        }

        return jwtTokenProvider.generateToken(member.getUserId(), member.getRoles());
    }

    @Override
    public MemberInfo getMemberProfile(String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));

        return MemberInfo.builder()
                .userId(member.getUserId())
                .userName(member.getUsername())
                .points(member.getPoints())
                .build();
    }

    @Override
    public MemberInfo getMemberPoints(String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));

        return MemberInfo.builder()
                .userId(member.getUserId())
                .userName(member.getUsername())
                .points(member.getPoints())
                .build();
    }
}

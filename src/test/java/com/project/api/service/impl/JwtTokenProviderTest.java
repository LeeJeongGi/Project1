package com.project.api.service.impl;

import com.project.api.config.security.JwtTokenProvider;
import com.project.api.dto.response.TokenInfo;
import com.project.api.entity.Member;
import com.project.api.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    private static Member pre_member;

    @BeforeEach
    void setting() {
        pre_member = Member.builder()
                .userId("JeongGi")
                .pw("1234")
                .userName("이정기")
                .build();

        memberRepository.save(pre_member);
    }

    @Test
    @DisplayName("토큰이 올바르게 생성된다.")
    void createToken() {
        final TokenInfo token = jwtTokenProvider.generateToken(pre_member.getUserId(), pre_member.getRoles());

        System.out.println("token = " + token);

        assertEquals(token.getAccessToken().isEmpty(), false);
    }

    @DisplayName("올바른 토큰 정보로 payload를 조회한다.")	// 6
    @Test
    void getPayloadByValidToken() {
        final TokenInfo token = jwtTokenProvider.generateToken(pre_member.getUserId(), pre_member.getRoles());

        Authentication authentication = jwtTokenProvider.getAuthentication(token.getAccessToken());

//        assertEquals(authentication.getPrincipal().equals(pre_member), true);
    }
}

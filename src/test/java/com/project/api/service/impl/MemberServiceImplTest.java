package com.project.api.service.impl;

import com.project.api.config.security.JwtTokenProvider;
import com.project.api.dto.request.LoginForm;
import com.project.api.dto.request.MemberForm;
import com.project.api.dto.response.MemberInfo;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MemberServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void cleanUp() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void saveMember() {
        // given
        MemberForm memberForm = MemberForm.builder()
                .userId("jeonggi")
                .pw("1234")
                .userName("이정기")
                .build();

        // when
        String saveId = memberService.saveMember(memberForm);

        // then
        assertEquals(memberForm.getUserId(), saveId);
    }

    @Test
    @DisplayName("회원 가입시 동일 id에 대한 에러 테스트")
    void saveMemberError() {
        // given
        Member pre_member = Member.builder()
                .userId("JeongGi")
                .pw("1234")
                .userName("이정기")
                .build();
        memberRepository.save(pre_member);

        MemberForm member = MemberForm.builder()
                .userId("JeongGi")
                .pw("324")
                .userName("이장기")
                .build();

        // expected
        assertThrows(IllegalArgumentException.class, () -> {
            memberService.saveMember(member);
        });
    }

    @Test
    @DisplayName("로그인 성공시 JWT 토큰 정상적으로 가져오는지 테스트")
    void login() {
        // given
        MemberForm pre_member = MemberForm.builder()
                .userId("jeonggi")
                .pw("1234")
                .userName("이정기")
                .build();

        String saveId = memberService.saveMember(pre_member);

        LoginForm loginForm = LoginForm.builder()
                .userId(pre_member.getUserId())
                .pw(pre_member.getPw())
                .build();

        // when
        TokenInfo token = memberService.login(loginForm);
        Authentication authentication = jwtTokenProvider.getAuthentication(token.getAccessToken());
        Member member = (Member) authentication.getPrincipal();

        // then
        assertEquals(member.getUserId(), pre_member.getUserId());
        assertEquals(member.getUsername(), pre_member.getUserName());
    }

    @Test
    @DisplayName("로그인 시 비밀 번호 다르면 실패하는 테스트")
    void loginError() {
        // given
        MemberForm pre_member = MemberForm.builder()
                .userId("jeonggi")
                .pw("1234")
                .userName("이정기")
                .build();

        String saveId = memberService.saveMember(pre_member);

        LoginForm loginForm = LoginForm.builder()
                .userId(pre_member.getUserId())
                .pw(pre_member.getPw() + "12")
                .build();

        // expected
        assertThrows(IllegalArgumentException.class, () -> {
            memberService.login(loginForm);
        });
    }

    @Test
    @DisplayName("회원 조회 테스트")
    void getMemberProfile() {
        // given
        MemberForm memberForm = MemberForm.builder()
                .userId("jeonggi")
                .pw("1234")
                .userName("이정기")
                .build();

        String saveId = memberService.saveMember(memberForm);

        // when
        MemberInfo profileMember = memberService.getMemberProfile(saveId);

        // then
        assertEquals(memberForm.getUserId(), profileMember.getUserId());
        assertEquals(memberForm.getUserName(), profileMember.getUserName());
    }

    @Test
    @DisplayName("회원 포인트 조회 테스트")
    void getMemberPoints() {
        // given
        MemberForm memberForm = MemberForm.builder()
                .userId("jeonggi")
                .pw("1234")
                .userName("이정기")
                .build();

        String saveId = memberService.saveMember(memberForm);

        // when
        MemberInfo memberInfo = memberService.getMemberPoints(saveId);

        // then
        assertEquals(0, memberInfo.getPoints());
    }

}
package com.project.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.dto.request.LoginForm;
import com.project.api.dto.request.MemberForm;
import com.project.api.dto.response.TokenInfo;
import com.project.api.repository.MemberRepository;
import com.project.api.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberServiceImpl memberService;

    @BeforeEach
    void cleanUp() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void saveMember() throws Exception {
        //given
        MemberForm form = MemberForm.builder()
                .userId("jeonggi")
                .pw("1234")
                .userName("이정기")
                .build();

        String json = objectMapper.writeValueAsString(form);

        // expected
        mockMvc.perform(post("/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(form.getUserId()))
                .andDo(print());

    }

    @Test
    @DisplayName("회원 가입시 아이디 입력 안하면 에러 발생 테스트")
    void saveMemberErrorById() throws Exception {
        //given
        MemberForm form = MemberForm.builder()
                .pw("1234")
                .userName("이정기")
                .build();

        String json = objectMapper.writeValueAsString(form);

        // expected
        mockMvc.perform(post("/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 가입시 비밀번호 입력 안하면 에러 발생 테스트")
    void saveMemberErrorByPw() throws Exception {
        //given
        MemberForm form = MemberForm.builder()
                .userId("jeonggi")
                .userName("이정기")
                .build();

        String json = objectMapper.writeValueAsString(form);

        // expected
        mockMvc.perform(post("/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 가입시 이름 입력 안하면 에러 발생 테스트")
    void saveMemberErrorByName() throws Exception {
        //given
        MemberForm form = MemberForm.builder()
                .userId("jeonggi")
                .pw("1234")
                .build();

        String json = objectMapper.writeValueAsString(form);

        // expected
        mockMvc.perform(post("/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 로그인 테스트")
    void loginMember() throws Exception {
        //given
        MemberForm member = MemberForm.builder()
                .userId("jeonggi")
                .pw("1234")
                .userName("이정기")
                .build();

        memberService.saveMember(member);

        LoginForm loginForm = LoginForm.builder()
                .userId(member.getUserId())
                .pw(member.getPw())
                .build();

        String json = objectMapper.writeValueAsString(loginForm);

        // expected
        mockMvc.perform(post("/signin")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.grantType").value("Bearer"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 조회 테스트")
    void getMemberProfile() throws Exception {
        //given
        MemberForm member = MemberForm.builder()
                .userId("jeonggi")
                .pw("1234")
                .userName("이정기")
                .build();

        memberService.saveMember(member);

        LoginForm loginForm = LoginForm.builder()
                .userId(member.getUserId())
                .pw(member.getPw())
                .build();

        String json = objectMapper.writeValueAsString(loginForm);

        TokenInfo token = memberService.login(loginForm);

        // expected
        mockMvc.perform(get("/profile")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", token.getAccessToken())
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.userId").value(member.getUserId()))
                .andExpect(jsonPath("$.response.userName").value(member.getUserName()))
                .andDo(print());
    }

    @Test
    @DisplayName("토큰 없이 회원 조회 할 때 발생하는 에러 테스트")
    void getMemberProfileError() throws Exception {
        //given
        MemberForm member = MemberForm.builder()
                .userId("jeonggi")
                .pw("1234")
                .userName("이정기")
                .build();

        memberService.saveMember(member);

        LoginForm loginForm = LoginForm.builder()
                .userId(member.getUserId())
                .pw(member.getPw())
                .build();

        String json = objectMapper.writeValueAsString(loginForm);

        // expected
        mockMvc.perform(get("/profile")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 포링트 조회 테스트")
    void getMemberPoints() throws Exception {
        //given
        MemberForm member = MemberForm.builder()
                .userId("jeonggi")
                .pw("1234")
                .userName("이정기")
                .build();

        memberService.saveMember(member);

        LoginForm loginForm = LoginForm.builder()
                .userId(member.getUserId())
                .pw(member.getPw())
                .build();

        String json = objectMapper.writeValueAsString(loginForm);

        TokenInfo token = memberService.login(loginForm);

        // expected
        mockMvc.perform(get("/points")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", token.getAccessToken())
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.points").value(0))
                .andDo(print());
    }
}
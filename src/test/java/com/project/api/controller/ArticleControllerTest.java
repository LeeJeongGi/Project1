package com.project.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.config.security.JwtTokenProvider;
import com.project.api.dto.request.ArticleForm;
import com.project.api.dto.response.TokenInfo;
import com.project.api.entity.Article;
import com.project.api.entity.Comments;
import com.project.api.entity.Member;
import com.project.api.repository.ArticleRepository;
import com.project.api.repository.CommentsRepository;
import com.project.api.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static Member member;

    private static TokenInfo token;

    @BeforeEach
    void cleanUp() {
        articleRepository.deleteAll();
        memberRepository.deleteAll();

        settingMember();
        settingToken();
    }

    @Test
    @DisplayName("??? ?????? ?????????")
    void saveArticle() throws Exception {
        //given
        ArticleForm form = ArticleForm.builder()
                .title("Test Title")
                .contents("Test Content")
                .build();

        String json = objectMapper.writeValueAsString(form);

        // expected
        mockMvc.perform(post("/article")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", token.getAccessToken())
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("??? ????????? ?????? ?????? ????????? ?????? ?????? ?????????")
    void saveMemberErrorByTitle() throws Exception {
        //given
        ArticleForm form = ArticleForm.builder()
                .contents("Test Content")
                .build();

        String json = objectMapper.writeValueAsString(form);

        // expected
        mockMvc.perform(post("/article")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", token.getAccessToken())
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("??? ????????? ?????? ?????? ????????? ?????? ?????? ?????????")
    void saveMemberErrorByContent() throws Exception {
        //given
        ArticleForm form = ArticleForm.builder()
                .title("Test Title")
                .build();

        String json = objectMapper.writeValueAsString(form);

        // expected
        mockMvc.perform(post("/article")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", token.getAccessToken())
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("??? ?????? ?????????")
    void modifyArticle() throws Exception {
        //given
        Article article = Article.builder()
                .member(member)
                .title("Title")
                .contents("Content")
                .build();
        Article saveArticle = articleRepository.save(article);

        ArticleForm form = ArticleForm.builder()
                .id(saveArticle.getId())
                .title("Modify Title")
                .contents("Modify Content")
                .build();
        String json = objectMapper.writeValueAsString(form);

        // expected
        mockMvc.perform(put("/article")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", token.getAccessToken())
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(saveArticle.getId()))
                .andDo(print());
    }

    @Test
    @DisplayName("??? ?????? ?????????")
    void getArticle() throws Exception {
        //given
        Article article = Article.builder()
                .title("Test Title")
                .contents("Content")
                .member(member)
                .build();

        Article saveArticle = articleRepository.save(article);

        ArrayList<Member> members = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            members.add(Member.builder()
                    .userId("test" + i)
                    .pw("123" + i)
                    .points(0)
                    .userName("?????????" + i)
                    .build());
        }
        memberRepository.saveAll(members);

        ArrayList<Comments> comments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            comments.add(Comments.builder()
                    .article(saveArticle)
                    .member(members.get(i))
                    .contents("????????? ?????? ????????????.!! " + i)
                    .build());
        }
        commentsRepository.saveAll(comments);

        // expected
        mockMvc.perform(get("/article/{article}", saveArticle.getId())
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", token.getAccessToken())
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("??? ?????? ?????????")
    void deleteArticle() throws Exception {
        //given
        Article article = Article.builder()
                .title("Test Title")
                .contents("Content")
                .member(member)
                .build();

        Article saveArticle = articleRepository.save(article);

        ArrayList<Member> members = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            members.add(Member.builder()
                    .userId("test" + i)
                    .pw("123" + i)
                    .points(0)
                    .userName("?????????" + i)
                    .build());
        }
        memberRepository.saveAll(members);

        ArrayList<Comments> comments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            comments.add(Comments.builder()
                    .article(saveArticle)
                    .member(members.get(i))
                    .contents("????????? ?????? ????????????.!! " + i)
                    .build());
        }
        commentsRepository.saveAll(comments);

        // expected
        mockMvc.perform(delete("/article/{article}", saveArticle.getId())
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", token.getAccessToken())
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    public void settingMember() {
        member = Member.builder()
                .userId("jeonggi")
                .pw("1234")
                .userName("?????????")
                .points(0)
                .build();

        memberRepository.save(member);
    }

    public void settingToken() {
        token = jwtTokenProvider.generateToken(member.getUserId(), member.getRoles());
    }
}
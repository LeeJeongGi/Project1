package com.project.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.config.security.JwtTokenProvider;
import com.project.api.dto.request.CommentsForm;
import com.project.api.dto.response.TokenInfo;
import com.project.api.entity.Article;
import com.project.api.entity.Comments;
import com.project.api.entity.Member;
import com.project.api.repository.ArticleRepository;
import com.project.api.repository.CommentsRepository;
import com.project.api.repository.MemberRepository;
import com.project.api.service.impl.CommentsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CommentsControllerTest {

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
    CommentsServiceImpl commentsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static Member member;

    private static Article article;

    private static TokenInfo token;

    private static Long articleId;

    @BeforeEach
    void cleanUp() {
        commentsRepository.deleteAll();
        articleRepository.deleteAll();
        memberRepository.deleteAll();

        settingMember();
        settingArticle();
        settingToken();
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    void createComments() throws Exception {
        //given
        Member commentMember = Member.builder()
                .userId("temp")
                .pw("321")
                .userName("댓글남")
                .points(0)
                .build();

        memberRepository.save(commentMember);

        CommentsForm form = CommentsForm.builder()
                .articleId(articleId)
                .contents("너무 좋은 글이네요")
                .build();

        String json = objectMapper.writeValueAsString(form);

        // expected
        mockMvc.perform(post("/comments")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", token.getAccessToken())
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.size()").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteComments() throws Exception {
        //given
        Member commentMember = Member.builder()
                .userId("temp")
                .pw("321")
                .userName("댓글남")
                .points(0)
                .build();

        memberRepository.save(commentMember);

        Comments comments = Comments.builder()
                .member(commentMember)
                .article(article)
                .contents("Test")
                .build();

        Comments saveComments = commentsRepository.save(comments);

        // 본인의 댓글만 삭제 가능하기 때문에 토큰 변경
        token = jwtTokenProvider.generateToken(commentMember.getUserId(), commentMember.getRoles());

        // expected
        mockMvc.perform(delete("/comments/{commentsId}", saveComments.getId())
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
                .userName("이정기")
                .points(0)
                .build();

        memberRepository.save(member);
    }

    public void settingToken() {
        token = jwtTokenProvider.generateToken(member.getUserId(), member.getRoles());
    }

    public void settingArticle() {
        article = Article.builder()
                .title("Test Title")
                .contents("Content")
                .member(member)
                .build();

        articleId = articleRepository.save(article).getId();
    }
}
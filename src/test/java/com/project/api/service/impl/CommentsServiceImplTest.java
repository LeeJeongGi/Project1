package com.project.api.service.impl;

import com.project.api.common.exception.NotFoundException;
import com.project.api.dto.request.CommentsForm;
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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CommentsServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private CommentsServiceImpl commentsService;

    private static Member member;

    private static Article article;

    @BeforeEach
    void cleanUp() {
        articleRepository.deleteAll();
        memberRepository.deleteAll();

        settingMember();
        settingArticle();
    }

    @Test
    @DisplayName("댓글 작성 테스트")
    void saveComments() {
        // given
        CommentsForm form = CommentsForm.builder()
                .articleId(article.getId())
                .contents("Test")
                .build();

        Member commentMember = Member.builder()
                .userId("temp")
                .pw("321")
                .userName("댓글남")
                .points(0)
                .build();

        memberRepository.save(commentMember);

        // when
        List<Long> commentsId = commentsService.saveComments(form, commentMember.getUserId());

        Comments comments = commentsRepository.findById(commentsId.get(0))
                .orElseThrow(() -> new NotFoundException(""));

        Member findMember = memberRepository.findByUserId(member.getUserId())
                .orElseThrow(() -> new NotFoundException(""));

        Member commnetMember = memberRepository.findByUserId(commentMember.getUserId())
                .orElseThrow(() -> new NotFoundException(""));

        // then
        assertThat(findMember.getPoints()).isEqualTo(1);
        assertThat(commnetMember.getPoints()).isEqualTo(2);
        assertThat(commentsId.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteComments() {
        // given
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

        // when
        Long deleteComments = commentsService.deleteComments(saveComments.getId(), commentMember.getUserId());

        Member findMember = memberRepository.findByUserId(member.getUserId())
                .orElseThrow(() -> new NotFoundException(""));

        Member commnetMember = memberRepository.findByUserId(commentMember.getUserId())
                .orElseThrow(() -> new NotFoundException(""));

        // then
        assertThat(findMember.getPoints()).isEqualTo(-1);
        assertThat(commnetMember.getPoints()).isEqualTo(-2);
        assertThat(commentsRepository.count()).isEqualTo(0);
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

    public void settingArticle() {
        article = Article.builder()
                .title("Test Title")
                .contents("Content")
                .member(member)
                .build();

        articleRepository.save(article);
    }

}
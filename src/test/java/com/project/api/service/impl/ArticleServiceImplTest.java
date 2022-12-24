package com.project.api.service.impl;

import com.project.api.common.exception.NotFoundException;
import com.project.api.dto.request.ArticleForm;
import com.project.api.dto.response.ArticleInfo;
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

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ArticleServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private ArticleServiceImpl articleService;

    private static Member member;

    @BeforeEach
    void cleanUp() {
        articleRepository.deleteAll();
        memberRepository.deleteAll();

        settingMember();
    }

    @Test
    @DisplayName("글 작성 테스트")
    void saveArticle() {
        // given
        ArticleForm articleForm = ArticleForm.builder()
                .title("Test Title")
                .contents("Content")
                .build();

        // when
        Long articleId = articleService.saveArticle(articleForm, member.getUserId());

        Article findArticle = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException(""));

        Member findMember = memberRepository.findByUserId(member.getUserId())
                .orElseThrow(() -> new NotFoundException(""));

        // then
        assertThat(findMember.getPoints()).isEqualTo(3);
        assertThat(findArticle.getTitle()).isEqualTo(articleForm.getTitle());
        assertThat(findArticle.getContents()).isEqualTo(articleForm.getContents());
    }

    @Test
    @DisplayName("글 수정 테스트")
    void modifyArticle() {
        // given
        ArticleForm articleForm = ArticleForm.builder()
                .title("Test Title")
                .contents("Content")
                .build();

        Long articleId = articleService.saveArticle(articleForm, member.getUserId());

        ArticleForm modifyArticle = ArticleForm.builder()
                .id(articleId)
                .title("Modify Titme")
                .contents("Modify Content")
                .build();

        // when
        Long modifyArticleId = articleService.modifyArticle(modifyArticle, member.getUserId());

        Article findArticle = articleRepository.findById(modifyArticleId)
                .orElseThrow(() -> new NotFoundException(""));

        // then
        assertThat(findArticle.getId()).isEqualTo(modifyArticle.getId());
        assertThat(findArticle.getTitle()).isEqualTo(modifyArticle.getTitle());
        assertThat(findArticle.getContents()).isEqualTo(modifyArticle.getContents());
    }

    @Test
    @DisplayName("작성자가 아닌 사람의 글을 수정 할 시 에러 발생")
    void modifyArticleError() {
        // given
        ArticleForm articleForm = ArticleForm.builder()
                .title("Test Title")
                .contents("Content")
                .build();

        Long articleId = articleService.saveArticle(articleForm, member.getUserId());

        ArticleForm modifyArticle = ArticleForm.builder()
                .id(articleId)
                .title("Modify Titme")
                .contents("Modify Content")
                .build();

        Member tempMember = Member.builder()
                .points(0)
                .userId("temp")
                .pw("1234")
                .userName("임시")
                .build();

        memberRepository.save(tempMember);

        // expected
        assertThrows(IllegalArgumentException.class, () -> {
            articleService.modifyArticle(modifyArticle, tempMember.getUserId());
        });

    }

    @Test
    @DisplayName("글 조회 테스트")
    void getArticle() {
        // given
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
                    .userName("댓글남" + i)
                    .build());
        }
        memberRepository.saveAll(members);

        ArrayList<Comments> comments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            comments.add(Comments.builder()
                    .article(saveArticle)
                    .member(members.get(i))
                    .contents("유익한 정보 였습니다.!! " + i)
                    .build());
        }
        commentsRepository.saveAll(comments);

        // when
        ArticleInfo response = articleService.getArticle(saveArticle.getId());

        // then
        assertThat(response.getArticleId()).isEqualTo(saveArticle.getId());
        assertThat(response.getContentsId().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("글 삭제 테스트")
    void deleteArticle() {
        // given
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
                    .userName("댓글남" + i)
                    .build());
        }
        memberRepository.saveAll(members);

        ArrayList<Comments> comments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            comments.add(Comments.builder()
                    .article(saveArticle)
                    .member(members.get(i))
                    .contents("유익한 정보 였습니다.!! " + i)
                    .build());
        }
        commentsRepository.saveAll(comments);

        // when
        Long deleteArticleId = articleService.deleteArticle(saveArticle.getId(), member.getUserId());

        // then
        assertThat(deleteArticleId).isEqualTo(saveArticle.getId());
        assertThat(commentsRepository.count()).isEqualTo(0);
        assertThat(articleRepository.count()).isEqualTo(0);
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

}
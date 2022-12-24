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
import com.project.api.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final String NOT_FOUND_MEMBER = "존재하지 않는 유저 입니다.";

    private final String NOT_FOUND_ARTICLE = "존재하지 않는 게시글 입니다.";

    private final String NOT_MODIFY_ARTICLE = "작성자가 아니면 글을 수정할 수 없습니다.";

    private final String NOT_DELETE_ARTICLE = "작성자가 아니면 글을 삭제할 수 없습니다.";

    private final int WRITE_POINT = 3;

    private final int WRITER_POINT = 2;

    private final int AUTHORSHIP_POINT = 1;

    private final ArticleRepository articleRepository;

    private final MemberRepository memberRepository;

    private final CommentsRepository commentsRepository;

    @Override
    public Long saveArticle(ArticleForm request, String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));

        member.plusPoint(WRITE_POINT);
        memberRepository.save(member);

        Article article = Article.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .member(member)
                .build();

        return articleRepository.save(article).getId();
    }

    @Override
    public ArticleInfo getArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTICLE));

        List<Comments> comments = commentsRepository.findByArticleId(article.getId());
        List<Long> commentsIds = comments.stream().map(Comments::getId).collect(Collectors.toList());

        return ArticleInfo.builder()
                .articleId(article.getId())
                .contentsId(commentsIds)
                .build();
    }

    @Override
    public Long modifyArticle(ArticleForm request, String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));

        Article article = articleRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTICLE));

        if (!article.getMember().getUserId().equals(member.getUserId())) {
            throw new IllegalArgumentException(NOT_MODIFY_ARTICLE);
        }

        article.modify(request);

        return articleRepository.save(article).getId();
    }

    @Override
    @Transactional
    public Long deleteArticle(Long articleId, String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTICLE));

        if (!article.getMember().getUserId().equals(member.getUserId())) {
            throw new IllegalArgumentException(NOT_DELETE_ARTICLE);
        }

        // 최초 작성시 작성자 +3점을 획득하기 때문에 작성자 -3점
        article.writerMinusPoint(WRITE_POINT);
        articleRepository.save(article);

        List<Comments> comments = commentsRepository.findByArticleId(article.getId());
        for (int i = 0; i < comments.size(); i++) {
            comments.get(i).minusPoint(WRITER_POINT);
            comments.get(i).getArticle().writerMinusPoint(AUTHORSHIP_POINT);
        }

        commentsRepository.saveAll(comments);
        commentsRepository.deleteAll(comments);

        articleRepository.delete(article);

        return article.getId();
    }
}

package com.project.api.service.impl;

import com.project.api.common.exception.NotFoundException;
import com.project.api.dto.request.CommentsForm;
import com.project.api.entity.Article;
import com.project.api.entity.Comments;
import com.project.api.entity.Member;
import com.project.api.repository.ArticleRepository;
import com.project.api.repository.CommentsRepository;
import com.project.api.repository.MemberRepository;
import com.project.api.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    private final String NOT_FOUND_MEMBER = "존재하지 않는 유저 입니다.";

    private final String NOT_FOUND_ARTICLE = "존재하지 않는 게시글 입니다.";

    private final String NOT_FOUND_COMMENTS = "존재하지 않는 댓글 입니다.";

    private final String NOT_DELETE_COMMENTS = "본인의 댓글만 지울 수 있습니다.";

    private final int WRITER_POINT = 2;

    private final int AUTHORSHIP_POINT = 1;

    private final ArticleRepository articleRepository;

    private final MemberRepository memberRepository;

    private final CommentsRepository commentsRepository;


    @Override
    @Transactional
    public List<Long> saveComments(CommentsForm commentsForm, String userId) {

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));

        Article article = articleRepository.findById(commentsForm.getArticleId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTICLE));

        // 글 작성자와 댓글 작성자가 동일한 경우 +1 점 증가, 동일 하지 않으면 댓글 작성자 +2점 증가
        if (!member.getUserId().equals(article.getMember().getUserId())) {
            member.plusPoint(WRITER_POINT);
            memberRepository.save(member);
        }

        article.writerPlusPoint(AUTHORSHIP_POINT);
        articleRepository.save(article);

        Comments comments = Comments.builder()
                .article(article)
                .member(member)
                .contents(commentsForm.getContents())
                .build();

        commentsRepository.save(comments);

        List<Comments> commentsList = commentsRepository.findByArticleId(commentsForm.getArticleId());

        return commentsList.stream().map(Comments::getId).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long deleteComments(Long commentId, String userId) {

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));

        Comments comments = commentsRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMMENTS));

        // 1. 삭제할 수 있는 권한도 본인 댓글이여야 가능하다.
        if (!comments.getMember().getUserId().equals(member.getUserId())) {
            throw new IllegalArgumentException("NOT_DELETE_COMMENTS");
        }

        Article article = articleRepository.findById(comments.getArticle().getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTICLE));

        if (member.getUserId().equals(comments.getArticle().getMember().getUserId())) {
            // 3. 단) 글 작성자가 본인의 글을 제거 할땐 -1 한다.
            member.minusPoint(AUTHORSHIP_POINT);
        } else {
            // 2. 삭제 한다면 본인은 -2, 작성자는 -1 한다.
            member.minusPoint(WRITER_POINT);
            article.writerMinusPoint(AUTHORSHIP_POINT);
        }


        memberRepository.save(member);
        articleRepository.save(article);

        commentsRepository.delete(comments);

        return comments.getId();
    }
}

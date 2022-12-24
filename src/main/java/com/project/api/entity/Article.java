package com.project.api.entity;

import com.project.api.dto.request.ArticleForm;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String title;

    private String contents;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Comments> comments;

    public void modify(ArticleForm request) {
        this.title = request.getTitle();
        this.contents = request.getContents();
    }

    public void writerPlusPoint(int points) {
        this.member.plusPoint(points);
    }

    public void writerMinusPoint(int points) {
        this.member.minusPoint(points);
    }
}

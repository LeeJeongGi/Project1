package com.project.api.service;

import com.project.api.dto.request.ArticleForm;
import com.project.api.dto.response.ArticleInfo;

public interface ArticleService {

    Long saveArticle(ArticleForm request, String userId);

    ArticleInfo getArticle(Long articleId);

    Long modifyArticle(ArticleForm request, String userId);

    Long deleteArticle(Long articleId, String userId);
}

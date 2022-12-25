package com.project.api.controller;

import com.project.api.common.utils.ApiUtils.ApiResult;
import com.project.api.common.utils.CommonUtils;
import com.project.api.dto.request.ArticleForm;
import com.project.api.dto.response.ArticleInfo;
import com.project.api.entity.Member;
import com.project.api.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.project.api.common.utils.ApiUtils.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("article")
public class ArticleController {

    private final ArticleService articleService;

    private static Authentication authentication;

    @PostMapping
    @Operation(summary = "글 작성", description = "글 작성을 위한 API.", security = { @SecurityRequirement(name = "bearer-key")})
    public ApiResult<Long> createArticle(@RequestBody @Valid ArticleForm articleForm) {
        authentication = CommonUtils.getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        return success(
                articleService.saveArticle(articleForm, member.getUserId())
        );
    }

    @PutMapping
    @Operation(summary = "글 수정", description = "글 수정을 위한 API.", security = { @SecurityRequirement(name = "bearer-key")})
    public ApiResult<Long> modifyArticle(@RequestBody @Valid ArticleForm articleForm) {
        authentication = CommonUtils.getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        return success(
                articleService.modifyArticle(articleForm, member.getUserId())
        );
    }

    @GetMapping("/{articleId}")
    @Operation(summary = "글 조회", description = "글 조회를 위한 API.", security = { @SecurityRequirement(name = "bearer-key")})
    public ApiResult<ArticleInfo> getArticle(@PathVariable Long articleId) {
        return success(
                articleService.getArticle(articleId)
        );
    }

    @DeleteMapping("/{articleId}")
    @Operation(summary = "글 삭제", description = "글 삭제를 위한 API.", security = { @SecurityRequirement(name = "bearer-key")})
    public ApiResult<Long> deleteArticle(@PathVariable Long articleId) {
        authentication = CommonUtils.getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        return success(
                articleService.deleteArticle(articleId, member.getUserId())
        );
    }

}

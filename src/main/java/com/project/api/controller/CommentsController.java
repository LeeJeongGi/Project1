package com.project.api.controller;

import com.project.api.common.utils.ApiUtils.ApiResult;
import com.project.api.common.utils.CommonUtils;
import com.project.api.dto.request.CommentsForm;
import com.project.api.entity.Member;
import com.project.api.service.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.project.api.common.utils.ApiUtils.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentsController {

    private final CommentsService commentsService;

    private static Authentication authentication;

    @PostMapping
    @Operation(summary = "댓글 등록", description = "댓글 등록을 위한 API.", security = { @SecurityRequirement(name = "bearer-key")})
    public ApiResult<List<Long>> createComments(@RequestBody @Valid CommentsForm commentsForm) {
        authentication = CommonUtils.getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        return success(
                commentsService.saveComments(commentsForm, member.getUserId())
        );
    }

    @DeleteMapping("{commentsId}")
    @Operation(summary = "댓글 삭제", description = "댓글 삭제를 위한 API.", security = { @SecurityRequirement(name = "bearer-key")})
    public ApiResult<Long> deleteComments(@PathVariable Long commentsId) {
        authentication = CommonUtils.getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        return success(
                commentsService.deleteComments(commentsId, member.getUserId())
        );
    }

}

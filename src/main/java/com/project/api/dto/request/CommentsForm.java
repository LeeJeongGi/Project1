package com.project.api.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentsForm {

    @NotNull(message = "게시글 ID는 필수 입력 값입니다.")
    private Long articleId;

    @NotBlank(message = "댓글 내용은 필수 입력 값입니다.")
    private String contents;
}

package com.project.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleForm {

    @JsonProperty("articleId")
    private Long id;

    @NotBlank(message = "글 제목은 필수 입력 값입니다.")
    @JsonProperty("articleTitle")
    private String title;

    @NotBlank(message = "글 내용은 필수 입력 값입니다.")
    @JsonProperty("articleContents")
    private String contents;
}

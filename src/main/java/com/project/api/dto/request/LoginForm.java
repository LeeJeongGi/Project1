package com.project.api.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {

    @NotBlank(message = "로그인 ID는 필수 입력 값입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String pw;
}

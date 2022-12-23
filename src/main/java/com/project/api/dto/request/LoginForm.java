package com.project.api.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@NoArgsConstructor
public class LoginForm {

    @NotBlank(message = "로그인 ID는 필수 입력 값입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String pw;

    @Builder
    public LoginForm(String userId, String pw, String userName) {
        this.userId = userId;
        this.pw = pw;
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(this.userId, this.pw);
    }
}

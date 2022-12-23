package com.project.api.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@NoArgsConstructor
public class MemberForm {

    @NotBlank(message = "로그인 ID는 필수 입력 값입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String pw;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String userName;

    @Builder
    public MemberForm(String userId, String pw, String userName) {
        this.userId = userId;
        this.pw = pw;
        this.userName = userName;
    }
}

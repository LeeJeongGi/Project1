package com.project.api.controller;

import com.project.api.common.utils.ApiUtils.ApiResult;
import com.project.api.common.utils.CommonUtils;
import com.project.api.dto.request.LoginForm;
import com.project.api.dto.request.MemberForm;
import com.project.api.dto.response.MemberInfo;
import com.project.api.dto.response.TokenInfo;
import com.project.api.entity.Member;
import com.project.api.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.project.api.common.utils.ApiUtils.success;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "회원 가입을 위한 API.")
    public ApiResult<String> createMember(@RequestBody @Valid MemberForm memberForm) {
        return success(
                memberService.saveMember(memberForm)
        );
    }

    @PostMapping("/signin")
    @Operation(summary = "로그인", description = "로그인을 위한 API.")
    public ApiResult<TokenInfo> login(@RequestBody @Valid LoginForm loginForm) {
        return success(
                memberService.login(loginForm)
        );
    }

    @GetMapping("/profile")
    @Operation(summary = "회원 조회", description = "회사 조회를 위한 API.", security = { @SecurityRequirement(name = "bearer-key")})
    public ApiResult<MemberInfo> getMemberProfile() {
        Authentication authentication = CommonUtils.getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        return success(
                memberService.getMemberProfile(member.getUserId())
        );
    }

    @GetMapping("/points")
    @Operation(summary = "포인트 조회", description = "회원의 포인트를 조회하는 API.", security = { @SecurityRequirement(name = "bearer-key")})
    public ApiResult<MemberInfo> getMemberPoints() {
        Authentication authentication = CommonUtils.getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        return success(
                memberService.getMemberProfile(member.getUserId())
        );
    }

}

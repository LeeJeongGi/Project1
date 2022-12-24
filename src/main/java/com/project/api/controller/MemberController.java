package com.project.api.controller;

import com.project.api.common.utils.ApiUtils.ApiResult;
import com.project.api.common.utils.CommonUtils;
import com.project.api.dto.request.LoginForm;
import com.project.api.dto.request.MemberForm;
import com.project.api.dto.response.MemberInfo;
import com.project.api.dto.response.TokenInfo;
import com.project.api.entity.Member;
import com.project.api.service.MemberService;
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
    public ApiResult<String> createMember(@RequestBody @Valid MemberForm memberForm) {
        return success(
                memberService.saveMember(memberForm)
        );
    }

    @PostMapping("/signin")
    public ApiResult<TokenInfo> login(@RequestBody @Valid LoginForm loginForm) {
        return success(
                memberService.login(loginForm)
        );
    }

    @GetMapping("/profile")
    public ApiResult<MemberInfo> getMemberProfile() {
        Authentication authentication = CommonUtils.getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        return success(
                memberService.getMemberProfile(member.getUserId())
        );
    }

    @GetMapping("/points")
    public ApiResult<MemberInfo> getMemberPoints() {
        Authentication authentication = CommonUtils.getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        return success(
                memberService.getMemberProfile(member.getUserId())
        );
    }

}

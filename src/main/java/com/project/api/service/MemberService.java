package com.project.api.service;

import com.project.api.dto.request.LoginForm;
import com.project.api.dto.request.MemberForm;
import com.project.api.dto.response.MemberInfo;
import com.project.api.dto.response.TokenInfo;

public interface MemberService {

    String saveMember(MemberForm request);

    TokenInfo login(LoginForm loginForm);

    MemberInfo getMemberProfile(String userId);

    MemberInfo getMemberPoints(String userId);
}

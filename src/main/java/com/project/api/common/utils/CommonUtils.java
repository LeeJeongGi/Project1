package com.project.api.common.utils;

import com.project.api.common.exception.UnauthorizedException;
import com.project.api.entity.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CommonUtils {

    private static final String UNAUTHORIZED_EXCEPTION = "토큰이 올바르지 않습니다.";

    public static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.getPrincipal().getClass().equals(Member.class)) {
            throw new UnauthorizedException(UNAUTHORIZED_EXCEPTION);
        }

        return authentication;
    }
}

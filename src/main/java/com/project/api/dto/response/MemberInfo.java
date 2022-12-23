package com.project.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberInfo {

    private String userId;

    private String userName;

    private int points;

    @Builder
    public MemberInfo(String userId, String userName, int points) {
        this.userId = userId;
        this.userName = userName;
        this.points = points;
    }
}

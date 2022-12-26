package com.project.api.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfo {

    private String userId;

    private String userName;

    private int points;

}

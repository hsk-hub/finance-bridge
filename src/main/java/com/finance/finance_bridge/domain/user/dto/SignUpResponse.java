package com.finance.finance_bridge.domain.user.dto;

import com.finance.finance_bridge.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignUpResponse {
    private Long userId;
    private String email;
    private String name;

    public static SignUpResponse from(User user) {
        return SignUpResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}

package org.example.moneylog.dto;

import lombok.Getter;
import org.example.moneylog.domain.User;

@Getter
public class SignupResponse {
    private final Long id;
    private final String email;
    private final String nickname;

    public SignupResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}

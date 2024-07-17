package com.imageplc.imageplace.dto;

import lombok.Getter;

@Getter
public class UserInfoDTO {
    private final String username;
    private final String email;
    private final String nickname;
    private final String resume;

    public UserInfoDTO(String username, String email, String nickname, String resume) {
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.resume = resume;
    }
}

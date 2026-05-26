package io.theurl.identity.application.dto;

import lombok.Data;

@Data
public class UserCreateRequestDto {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
}

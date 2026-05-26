package io.theurl.identity.application.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class UserProfileResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private Collection<String> roles;
}

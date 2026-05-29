package io.theurl.identity.application.dto;

import lombok.Data;

@Data
public class UserUpdateRequestDto {
    private String email;
    private String phone;
    private String nickname;
    private String code;
    private String requestId;
}

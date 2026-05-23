package io.theurl.identity.persistence.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserAuthInfo {
    private Long id;
    private String username;
    private String nickname;
    private String passwordHash;
    private String passwordSalt;
    private String email;
    private String phone;
    private LocalDateTime lockedUntil;
    private List<String> roles;
}

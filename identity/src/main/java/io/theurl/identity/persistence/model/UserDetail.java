package io.theurl.identity.persistence.model;

import lombok.Data;

import java.util.Collection;

@Data
public class UserDetail {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private Collection<String> roles;
}

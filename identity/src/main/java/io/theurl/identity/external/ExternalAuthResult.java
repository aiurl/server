package io.theurl.identity.external;

import lombok.Data;

@Data
public class ExternalAuthResult {
    private String id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String avatarUrl;
}

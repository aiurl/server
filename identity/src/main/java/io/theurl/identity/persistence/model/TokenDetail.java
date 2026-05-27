package io.theurl.identity.persistence.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TokenDetail {
    private Long id;
    private String jti;
    private String content;
    private Long subject;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime refreshAt;
    private LocalDateTime revokedAt;
}

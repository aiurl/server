package io.theurl.identity.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "token", indexes = {
    @Index(name = "idx_token_jti", columnList = "jti", unique = true),
    @Index(name = "idx_token_subject", columnList = "subject")
})
public class Token implements Persistable<Long> {
    @Id
    private Long id;

    @Column(length = 36)
    private String jti;

    @Column(length = 1000)
    private String content;

    @Column
    private Long subject;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @Column(length = 20)
    private String status;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}

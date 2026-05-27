package io.theurl.identity.domain.aggregate;

import io.theurl.framework.domain.AggregateRoot;
import io.theurl.framework.utility.SnowflakeId;

import java.time.LocalDateTime;

@SuppressWarnings({"LombokGetterMayBeUsed"})
public class Token extends AggregateRoot<Long> {
    private final String jti;
    private final String content;
    private final Long subject;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime refreshAt;
    private LocalDateTime revokedAt;

    /**
     * Initializes the aggregate with the given id.
     *
     * @param id the identifier of the aggregate
     */
    public Token(Long id, String jti, String content, Long subject) {
        super(id);
        this.jti = jti;
        this.content = content;
        this.subject = subject;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public String getContent() {
        return content;
    }

    public String getJti() {
        return jti;
    }

    public Long getSubject() {
        return subject;
    }

    public LocalDateTime getRefreshAt() {
        return refreshAt;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        if (issuedAt != null && issuedAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("issuedAt must be in the future");
        }
        this.issuedAt = issuedAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        if (expiresAt != null && expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("expiresAt must be in the future");
        }
        this.expiresAt = expiresAt;
    }

    public void refresh() {
        this.refreshAt = LocalDateTime.now();
    }

    public void revoke() {
        this.revokedAt = LocalDateTime.now();
    }

    public static Token create(String jti, String content, Long subject) {
        var id = SnowflakeId.getInstance().nextId();
        return new Token(id, jti, content, subject);
    }
}

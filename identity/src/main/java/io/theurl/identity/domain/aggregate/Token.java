package io.theurl.identity.domain.aggregate;

import io.theurl.framework.domain.AggregateRoot;
import io.theurl.framework.utility.SnowflakeId;

import java.time.LocalDateTime;

@SuppressWarnings({"LombokGetterMayBeUsed"})
public class Token extends AggregateRoot<Long> {
    private String jti;
    private String content;
    private Long subject;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;

    /**
     * Initializes the aggregate with the given id.
     *
     * @param id the identifier of the aggregate
     */
    public Token(Long id, String jti, String content, Long subject) {
        super(id);
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        if (issuedAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("issuedAt must be in the future");
        }
        this.issuedAt = issuedAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("expiresAt must be in the future");
        }
        this.expiresAt = expiresAt;
    }

    public static Token create(String jti, String content, Long subject) {
        var id = SnowflakeId.getInstance().nextId();
        return new Token(id, jti, content, subject);
    }
}

package io.theurl.identity.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Entity
@Table(name = "users", indexes = {
    @Index(name = "user_idx_username", columnList = "username", unique = true),
    @Index(name = "user_idx_email", columnList = "email", unique = true),
    @Index(name = "user_idx_phone", columnList = "phone", unique = true)
})
public class User implements Persistable<Long> {
    static final int USERNAME_LENGTH = 64;
    static final int PASSWORD_HASH_LENGTH = 1000;
    static final int PASSWORD_SALT_LENGTH = 32;

    @Id
    private Long id;

    @Column(unique = true, nullable = false, updatable = false, length = USERNAME_LENGTH)
    private String username;

    @Column(nullable = false, name = "password_hash", length = PASSWORD_HASH_LENGTH)
    private String passwordHash;

    @Column(nullable = false, name = "password_salt", length = PASSWORD_SALT_LENGTH)
    private String passwordSalt;

    @Column(length = 64, updatable = false)
    private String nickname;

    @Column(unique = true)
    private String email;

    @Column(unique = true, length = 25)
    private String phone;

    @Column(name = "access_failed_count", columnDefinition = "int default 0")
    private Integer accessFailedCount = 0;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Collection<UserRole> roles;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Collection<UserAuthority> authorities;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}

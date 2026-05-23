package io.theurl.identity.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_authority", indexes = {
    @Index(name = "user_authority_idx_unique", columnList = "provider,open_id,user_id", unique = true)
})
public class UserAuthority implements Persistable<Long> {
    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(length = 100)
    private String provider;

    @Column(length = 200)
    private String openId;

    @Column(length = 100)
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}

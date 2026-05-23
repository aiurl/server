package io.theurl.identity.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_role", indexes = {
    @Index(name = "user_role_idx_unique", columnList = "name,user_id", unique = true)
})
public class UserRole implements Persistable<Long> {
    @Id
    @Column(length = 20)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(name = "user_id")
    private Long userId;

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

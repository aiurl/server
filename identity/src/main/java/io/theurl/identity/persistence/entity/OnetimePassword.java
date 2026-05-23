package io.theurl.identity.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "onetime_password")
public class OnetimePassword implements Persistable<Long> {
    @Id
    private Long id;

    @Column(name = "request_id", nullable = false, unique = true, length = 64)
    private String requestId;

    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Column(name = "recipient", nullable = false)
    private String recipient;

    @Column(name = "expiration")
    private LocalDateTime expiration;

    @Column(name = "checked")
    private LocalDateTime checked;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "usage")
    private int usage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}

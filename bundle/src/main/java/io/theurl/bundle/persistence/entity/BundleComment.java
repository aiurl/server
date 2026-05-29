package io.theurl.bundle.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bundle_comment", indexes = {
    @jakarta.persistence.Index(name = "idx_bundle_comment_bundle_id", columnList = "bundleId"),
    @jakarta.persistence.Index(name = "idx_bundle_comment_author_id", columnList = "authorId"),
})
public class BundleComment implements Persistable<Long> {
    @Id
    private Long id;

    @Column(name = "bundle_id", nullable = false, updatable = false)
    private long bundleId;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "author_id", updatable = false)
    private Long authorId;

    @Column(name = "author_name", length = 100, nullable = false, updatable = false)
    private String authorName;

    @Column(name = "contact", length = 100)
    private String contact;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Override
    public @Nullable Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}

package io.theurl.bundle.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bundle_item", indexes = {
    @Index(name = "idx_bundle_item_bundle_id", columnList = "bundle_id"),
    @Index(name = "idx_bundle_item_url", columnList = "url"),
    @Index(name = "idx_bundle_item_order", columnList = "order")
})
public class BundleItem implements Persistable<Long> {
    @Id
    private Long id;

    @Column(name = "bundle_id", nullable = false, updatable = false)
    private long bundleId;

    @Column(name = "url", nullable = false, updatable = false, columnDefinition = "text")
    private String url;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "image", length = 500)
    private String image;

    @Column(name = "order", nullable = false)
    private int order;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bundle_id", insertable = false, updatable = false)
    private Bundle bundle;

    @Override
    public @Nullable Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}

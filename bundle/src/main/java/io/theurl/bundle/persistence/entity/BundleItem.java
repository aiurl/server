package io.theurl.bundle.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Persistable;

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

    @Column(name = "url", nullable = false, updatable = false)
    private String url;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "image", length = 500)
    private String image;

    @Column(name = "order", nullable = false)
    private int order;

    @Override
    public @Nullable Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}

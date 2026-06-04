package io.theurl.bundle.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Persistable;

@Entity
@Data
@Table(name = "bundle_label", indexes = {
    @Index(name = "idx_bundle_label_unique", columnList = "bundle_id,name")
})
public class BundleLabel implements Persistable<Long> {
    @Id
    private Long id;

    @Column(name = "bundle_id", nullable = false, updatable = false)
    private long bundleId;

    @Column(name = "name", nullable = false, updatable = false, length = 50)
    private String name;

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

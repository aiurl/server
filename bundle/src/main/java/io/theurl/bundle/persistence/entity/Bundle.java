package io.theurl.bundle.persistence.entity;

import io.theurl.bundle.domain.aggregate.BundleExtend;
import jakarta.persistence.*;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Persistable;

import java.util.Collection;

@Data
@Entity
@Table(name = "bundle", indexes = {
    @Index(name = "idx_bundle_type", columnList = "type"),
    @Index(name = "idx_bundle_name", columnList = "name"),
    @Index(name = "idx_bundle_vanity", columnList = "vanity", unique = true),
    @Index(name = "idx_bundle_owner_id", columnList = "ownerId"),
    @Index(name = "idx_bundle_order", columnList = "order")
})
public class Bundle implements Persistable<Long> {
    @Id
    private Long id;

    @Column(name = "type", length = 32, nullable = false, updatable = false)
    private String type;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "vanity", length = 32, unique = true)
    private String vanity;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "order")
    private int order;

    @Column(name = "owner_id", nullable = false, updatable = false)
    private Long ownerId;

    @Column(name = "owner_name", length = 100, nullable = false, updatable = false)
    private String ownerName;

    @OneToMany(mappedBy = "bundle", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bundle_id")
    private Collection<BundleItem> items;

    @OneToMany(mappedBy = "bundle", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bundle_id")
    private Collection<BundleComment> comments;

    @OneToOne(mappedBy = "bundle", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")
    private BundleExtend extend;

    @Column(name = "is_deleted")
    private boolean deleted;

    @Override
    public @Nullable Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}

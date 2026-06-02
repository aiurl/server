package io.theurl.bundle.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bundle_extend")
public class BundleExtend implements Persistable<Long> {
    @Id
    private Long id;

    @Column(name = "items_count")
    private int itemsCount;

    @Column(name = "favorite_count")
    private int favoriteCount;

    @Column(name = "comment_count")
    private int commentCount;

    @Column(name = "visit_count")
    private int visitCount;

    @Column(name = "last_visited_at")
    private LocalDateTime lastVisitedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
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

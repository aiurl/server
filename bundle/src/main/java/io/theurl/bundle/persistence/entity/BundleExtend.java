package io.theurl.bundle.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bundle_extend")
public class BundleExtend implements Persistable<Long> {
    private Long id;
    private int itemCount;
    private int favoriteCount;
    private int commentCount;
    private int visitCount;
    private LocalDateTime lastVisitedAt;

    @Override
    public @Nullable Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}

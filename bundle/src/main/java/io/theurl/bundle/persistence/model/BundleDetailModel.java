package io.theurl.bundle.persistence.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BundleDetailModel {
    private Long id;
    private String vanity;
    private String type;
    private String name;
    private String description;
    private Long ownerId;
    private String ownerName;
    private int itemCount;
    private int commentCount;
    private int favoriteCount;
    private int visitCount;
    private LocalDateTime lastVisitedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

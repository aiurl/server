package io.theurl.bundle.persistence.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BundleListModel {
    private long id;
    private String type;
    private String vanity;
    private String name;
    private String description;
    private String image;
    private int itemsCount;
    private int favoriteCount;
    private int commentCount;
    private int visitCount;
    private Long ownerId;
    private String ownerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

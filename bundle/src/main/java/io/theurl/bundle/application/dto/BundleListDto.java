package io.theurl.bundle.application.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class BundleListDto extends BundleBaseDto {
    private String type;
    private String vanity;
    private int itemsCount;
    private int favoriteCount;
    private int commentCount;
    private int visitCount;
    private Long ownerId;
    private String ownerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

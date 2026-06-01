package io.theurl.bundle.application.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BundleItemListDto {
    private long id;
    private String url;
    private String title;
    private String description;
    private int order;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

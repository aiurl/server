package io.theurl.bundle.persistence.model;

import lombok.Data;

@Data
public class BundleItemModel {
    private long id;
    private long bundleId;
    private String url;
    private String title;
    private String description;
    private String image;
    private int order;
}

package io.theurl.bundle.application.dto;

import lombok.Data;

@Data
public class BundleItemEditDto {
    private String url;
    private String title;
    private String description;
    private String image;
}

package io.theurl.bundle.application.dto;

import lombok.Data;

@Data
public abstract class BundleBaseDto {
    private String name;
    private String description;
    private String image;
}

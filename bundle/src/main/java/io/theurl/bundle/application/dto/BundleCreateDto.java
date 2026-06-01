package io.theurl.bundle.application.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO for creating a bundle. It extends BundleBaseDto and includes additional fields specific to bundle creation.
 * The fields include:
 * - type: The type of the bundle.
 * - vanity: A unique identifier for the bundle, often used in URLs.
 * This DTO is used when a client wants to create a new bundle and needs to provide the necessary information for the creation process.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BundleCreateDto extends BundleBaseDto {
    /**
     * The type of the bundle. This field is used to categorize the bundle and can be used for filtering and searching bundles based on their type.
     */
    private String type;
    /**
     * A unique identifier for the bundle, often used in URLs. This field is used to create a user-friendly and memorable URL for the bundle.
     */
    private String vanity;
}

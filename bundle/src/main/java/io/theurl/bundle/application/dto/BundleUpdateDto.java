package io.theurl.bundle.application.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO for updating a bundle. It extends BundleBaseDto and does not add any new fields, but it can be used to differentiate between create and update operations.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BundleUpdateDto extends BundleBaseDto {
}

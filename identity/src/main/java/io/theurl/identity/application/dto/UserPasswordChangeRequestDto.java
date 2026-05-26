package io.theurl.identity.application.dto;

import lombok.Data;

/**
 * The data transfer object for user password change request.
 */
@Data
public class UserPasswordChangeRequestDto {
    private String oldPassword;
    private String newPassword;
}

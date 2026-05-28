package io.theurl.shared.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserLockedEto {
    private String username;
    private String email;
    private String phone;
    private LocalDateTime lockedUntil;
}

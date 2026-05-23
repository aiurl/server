package io.theurl.identity.persistence.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OnetimePasswordDetail {
    private Long id;
    private String requestId;
    private String code;
    private String recipient;
    private LocalDateTime expiration;
    private LocalDateTime checked;
}

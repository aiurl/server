package io.theurl.shared.event;

import lombok.Data;

@Data
public class OnetimePasswordCreatedEto {
    private String requestId;
    private String recipient;
    private String code;
    private Integer duration;
    private String usage;
}

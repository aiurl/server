package io.theurl.identity.application.command;

import com.neroyun.mediator.Command;
import lombok.Data;

@Data
public class OnetimePasswordCreateCommand implements Command {
    private final String requestId;
    private final String recipient;
    private final String code;

    private String usage;
    private Integer duration;

    public OnetimePasswordCreateCommand(String requestId, String recipient, String code) {
        this.requestId = requestId;
        this.recipient = recipient;
        this.code = code;
    }
}

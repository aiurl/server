package io.theurl.identity.application.command;

import com.neroyun.mediator.Command;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TokenCreateCommand implements Command {
    private String jti;
    private String content;
    private Long subject;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
}

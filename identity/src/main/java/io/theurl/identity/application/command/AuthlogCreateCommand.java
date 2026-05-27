package io.theurl.identity.application.command;

import com.neroyun.mediator.Command;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Command to create an authentication log entry, capturing details of an authentication attempt.
 * This command is typically used after an authentication attempt (successful or failed) to record the event in the system for auditing and monitoring purposes.
 */
@Data
public class AuthlogCreateCommand implements Command {
    private Long userId;
    private String username;
    private String grantType;
    private String requestId;
    private String ipAddress;
    private String userAgent;
    private String referrer;
    private String appName;
    private String appVersion;
    private String osPlatform;
    private String source;
    private boolean success;
    private String remark;
    private LocalDateTime timestamp;
}

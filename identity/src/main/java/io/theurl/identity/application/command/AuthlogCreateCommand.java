package io.theurl.identity.application.command;

import com.neroyun.mediator.Command;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public final class AuthlogCreateCommand implements Command {
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

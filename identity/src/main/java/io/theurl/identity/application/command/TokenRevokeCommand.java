package io.theurl.identity.application.command;

import com.neroyun.mediator.Command;

public record TokenRevokeCommand(String jti, String reason) implements Command {
}

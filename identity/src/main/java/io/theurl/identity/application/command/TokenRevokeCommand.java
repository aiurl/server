package io.theurl.identity.application.command;

import com.neroyun.mediator.Command;
import io.theurl.identity.domain.enums.TokenStatus;

public record TokenRevokeCommand(String jti, TokenStatus status) implements Command {
}

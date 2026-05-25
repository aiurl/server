package io.theurl.identity.application.command;

import com.neroyun.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPasswordChangeCommand implements Command {
    private final Long userId;
    private final String password;
    private final String changeType;
}

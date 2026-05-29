package io.theurl.identity.application.command;

import com.neroyun.mediator.Command;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public final class UserUpdateCommand implements Command {
    @Getter
    private final Long id;

    @Getter
    private final Map<String, Object> modifications = new HashMap<>();

    public UserUpdateCommand(Long id) {
        this.id = id;
    }
}

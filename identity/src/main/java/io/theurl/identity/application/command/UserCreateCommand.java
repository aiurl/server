package io.theurl.identity.application.command;

import com.neroyun.mediator.Command;
import lombok.Data;

@Data
public final class UserCreateCommand implements Command {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
}

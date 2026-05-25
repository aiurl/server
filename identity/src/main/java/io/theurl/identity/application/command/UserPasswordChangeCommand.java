package io.theurl.identity.application.command;

import com.neroyun.mediator.Command;

/**
 * Command for changing user password. The changeType can be "reset" for resetting password or "update" for updating password.
 *
 * @param userId     the ID of the user whose password is to be changed
 * @param password   the new password for the user
 * @param changeType the type of password change, either "reset" or "update"
 */
public record UserPasswordChangeCommand(Long userId, String password, String changeType) implements Command {
}

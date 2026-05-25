package io.theurl.identity.application.command;

import com.neroyun.mediator.Command;

/**
 * Command to update the access failure count of a user.
 * This command is typically used when a user authentication attempt fails, and we want to increment the failure count for that user.
 * The command contains the user ID and the new failure count to be set.
 */
public record UserAccessFailureCountCommand(Long userId, String action) implements Command {

}

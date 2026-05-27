package io.theurl.identity.application.command;

import com.neroyun.mediator.Command;

/**
 * Command to remove external authority from a user.
 *
 * @param id       the ID of the user
 * @param provider the external authority provider
 * @param openId   the open ID of the external authority
 */
public record UserAuthorityRemoveCommand(Long id, String provider, String openId) implements Command {
}

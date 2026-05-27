package io.theurl.identity.application.command;

import com.neroyun.mediator.Command;

/**
 * Command to add external authority to a user.
 *
 * @param id       the ID of the user
 * @param provider the external authority provider
 * @param openId   the open ID of the external authority
 * @param name     the name of the external authority
 */
public record UserAuthorityCreateCommand(Long id, String provider, String openId, String name) implements Command {
}

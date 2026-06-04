package io.theurl.bundle.application.command;

import com.neroyun.mediator.Command;

public record BundleItemRemoveCommand(String vanity, long itemId) implements Command {
}

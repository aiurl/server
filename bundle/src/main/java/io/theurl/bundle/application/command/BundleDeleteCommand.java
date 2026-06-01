package io.theurl.bundle.application.command;

import com.neroyun.mediator.Command;

public record BundleDeleteCommand(String vanity) implements Command {
}

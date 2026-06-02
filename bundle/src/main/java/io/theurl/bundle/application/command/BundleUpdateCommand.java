package io.theurl.bundle.application.command;

import com.neroyun.mediator.Command;
import lombok.Data;

@Data
public class BundleUpdateCommand implements Command {
    private final String vanity;

    private String name;
    private String description;
    private String image;

    public BundleUpdateCommand(String vanity) {
        this.vanity = vanity;
    }
}

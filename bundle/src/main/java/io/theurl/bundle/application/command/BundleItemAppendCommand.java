package io.theurl.bundle.application.command;

import com.neroyun.mediator.Command;
import lombok.Data;

@Data
public class BundleItemAppendCommand implements Command {
    private final String vanity;

    public BundleItemAppendCommand(String vanity) {
        this.vanity = vanity;
    }

    private String url;
    private String title;
    private String description;
    private String image;
}

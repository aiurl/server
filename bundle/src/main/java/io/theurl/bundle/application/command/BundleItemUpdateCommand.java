package io.theurl.bundle.application.command;

import com.neroyun.mediator.Command;
import lombok.Data;

@Data
public class BundleItemUpdateCommand implements Command {
    private final String vanity;
    private final long itemId;

    public BundleItemUpdateCommand(String vanity, long itemId) {
        this.vanity = vanity;
        this.itemId = itemId;
    }

    private String title;
    private String description;
    private String image;
}

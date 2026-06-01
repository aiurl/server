package io.theurl.bundle.application.command;

import com.neroyun.mediator.Command;
import lombok.Data;

@Data
public class BundleCreateCommand implements Command {
    private final String type;
    private final String vanity;
    private String name;
    private String description;
    private String image;
    private Long ownerId;
    private String ownerName;

    public BundleCreateCommand(String type, String vanity) {
        this.type = type;
        this.vanity = vanity;
    }
}

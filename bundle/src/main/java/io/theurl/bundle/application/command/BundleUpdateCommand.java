package io.theurl.bundle.application.command;

import com.neroyun.mediator.Command;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class BundleUpdateCommand implements Command {
    private final String vanity;

    private String name;
    private String description;
    private String image;

    public BundleUpdateCommand(String vanity) {
        this.vanity = vanity;
    }

    public String getVanity() {
        return vanity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

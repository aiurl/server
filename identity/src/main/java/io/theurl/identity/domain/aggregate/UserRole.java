package io.theurl.identity.domain.aggregate;

import io.theurl.framework.domain.Entity;

@SuppressWarnings({"LombokGetterMayBeUsed"})
public class UserRole extends Entity<Long> {
    private final String name;

    public UserRole(Long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

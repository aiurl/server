package io.theurl.identity.domain.aggregate;

import io.theurl.framework.domain.Entity;

@SuppressWarnings({"LombokSetterMayBeUsed", "LombokGetterMayBeUsed"})
public class UserAuthority extends Entity<Long> {

    private final String provider;
    private final String openId;
    private String name;

    public UserAuthority(Long id, String provider, String openId) {
        super(id);
        this.provider = provider;
        this.openId = openId;
    }

    public String getProvider() {
        return provider;
    }

    public String getOpenId() {
        return openId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

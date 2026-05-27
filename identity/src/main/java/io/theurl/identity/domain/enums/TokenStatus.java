package io.theurl.identity.domain.enums;

public enum TokenStatus {
    NORMAL("normal"),
    LOGOUT("logout"),
    REFRESHED("refreshed");
    private final String value;

    TokenStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

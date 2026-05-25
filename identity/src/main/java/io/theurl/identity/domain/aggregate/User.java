package io.theurl.identity.domain.aggregate;

import io.theurl.framework.domain.AggregateRoot;
import io.theurl.framework.utility.RegexUtility;
import io.theurl.framework.utility.SnowflakeId;
import io.theurl.identity.domain.event.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("LombokSetterMayBeUsed")
@Getter
public class User extends AggregateRoot<Long> {
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String password;
    private Integer accessFailedCount;
    private LocalDateTime lockedUntil;
    private Collection<UserRole> roles = new HashSet<>();
    private Collection<UserAuthority> authorities = new HashSet<>();

    /**
     * Initializes the aggregate with the given id.
     *
     * @param id the identifier of the aggregate
     */
    public User(Long id) {
        super(id);
        registerEvent(UserPhoneChangedEvent.class, event -> this.phone = event.getNewPhone());
        registerEvent(UserEmailChangedEvent.class, event -> this.email = event.getNewEmail());
    }

    public static User create(String username) {
        var id = SnowflakeId.getInstance().nextId();
        var user = new User(id);
        user.setUsername(username);
        user.raiseEvent(new UserCreatedEvent(username));
        return user;
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase(Locale.ROOT);
    }

    public void setNickname(String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            throw new IllegalArgumentException("nickname cannot be null or empty");
        }

        if (Objects.equals(this.nickname, nickname)) {
            return;
        }

        this.nickname = nickname;
    }

    /**
     * Sets the email for the user.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email cannot be null or empty");
        }

        if (Objects.equals(this.email, email)) {
            return;
        }

        email = email.trim().toLowerCase(Locale.ROOT);
        if (RegexUtility.isEmail(email)) {
            raiseEvent(new UserEmailChangedEvent(this.getId(), this.email, email));
        } else {
            throw new IllegalArgumentException("email is not valid");
        }
    }

    /**
     * Sets the phone number for the user.
     *
     * @param phone the phone number to set
     */
    public void setPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("phone cannot be null or empty");
        }

        if (Objects.equals(this.phone, phone)) {
            return;
        }

        phone = phone.trim().toLowerCase(Locale.ROOT);
        if (RegexUtility.isPhone(phone)) {
            raiseEvent(new UserPhoneChangedEvent(this.getId(), this.phone, phone));
        } else {
            throw new IllegalArgumentException("phone is not valid");
        }
    }

    /**
     * Sets the password for the user.
     *
     * @param password   the password to set
     * @param changeType the type of change being made
     */
    public void setPassword(String password, String changeType) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("password cannot be null or empty");
        }

        this.password = password;

        if (!changeType.equals("init")) {
            raiseEvent(new UserPasswordChangedEvent(this.getId(), LocalDateTime.now(), changeType));
        }
    }

    public void setAccessFailedCount(Integer accessFailedCount) {
        this.accessFailedCount = accessFailedCount;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public void increaseAccessFailedCount() {
        this.accessFailedCount += 1;
        if (this.accessFailedCount >= 10) {
            var minutes = 15 * (this.accessFailedCount - 9);
            if (minutes > 3600 * 24) {
                minutes = 3600 * 24;
            }
            this.lockedUntil = LocalDateTime.now().plusMinutes(minutes);
            raiseEvent(new UserLockedEvent(this.getId(), this.lockedUntil));
        }
    }

    public void resetAccessFailedCount() {
        if (accessFailedCount == 0) {
            return;
        }
        var previousValue = this.accessFailedCount;

        this.accessFailedCount = 0;
        this.lockedUntil = null;

        if (previousValue > 0) {
            raiseEvent(new UserUnlockedEvent(this.getId(), this.lockedUntil));
        }

    }

    /**
     * Sets the roles for the user.
     * NOTES: this method is only defined for persistence purpose, it will override the existing roles with the given collection.
     *
     * @param roles the collection of roles to set
     */
    public void setRoles(Collection<UserRole> roles) {
        this.roles = roles;
    }

    public void addRole(String name) {
        if (roles.stream().anyMatch(r -> r.getName().equals(name))) {
            throw new IllegalArgumentException("role already exists");
        }

        var role = new UserRole(SnowflakeId.getInstance().nextId(), name);
        this.roles.add(role);
    }

    public void removeRole(String name) {
        if (roles.stream().anyMatch(r -> r.getName().equals(name))) {
            roles.removeIf(r -> r.getName().equals(name));
        } else {
            throw new IllegalArgumentException("role does not exist");
        }
    }

    public void addAuthorities(Collection<UserAuthority> authorities) {
        this.authorities = authorities;
    }

    public void addAuthority(String provider, String openId, String name) {
        if (authorities.stream().anyMatch(r -> r.getProvider().equals(provider) && r.getOpenId().equals(openId))) {
            throw new IllegalArgumentException("authority already exists");
        }

        var authority = new UserAuthority(SnowflakeId.getInstance().nextId(), provider, openId);
        authority.setName(name);
        this.authorities.add(authority);
    }

    public void removeAuthority(String provider, String openId) {
        if (authorities.stream().anyMatch(r -> r.getProvider().equals(provider) && r.getOpenId().equals(openId))) {
            authorities.removeIf(r -> r.getProvider().equals(provider) && r.getOpenId().equals(openId));
        } else {
            throw new IllegalArgumentException("authority does not exist");
        }
    }

}

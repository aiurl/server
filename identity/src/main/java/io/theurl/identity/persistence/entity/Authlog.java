package io.theurl.identity.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "authlog")
public class Authlog implements Persistable<Long> {
    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(length = User.USERNAME_LENGTH)
    private String username;

    @Column(name = "grant_type", length = 32)
    private String grantType;

    @Column(name = "request_id", length = 64)
    private String requestId;

    @Column(name = "ip_address", length = 15)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column
    private String referrer;

    @Column(name = "app_name", length = 64)
    private String appName;

    @Column(name = "app_version", length = 20)
    private String appVersion;

    @Column(name = "os_platform", length = 32)
    private String osPlatform;

    @Column(length = 32)
    private String source;

    @Column
    private boolean success;

    @Column(length = 500)
    private String remark;

    @Column
    private LocalDateTime timestamp;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return false;
    }


}

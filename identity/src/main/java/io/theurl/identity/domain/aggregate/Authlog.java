package io.theurl.identity.domain.aggregate;

import io.theurl.framework.domain.AggregateRoot;
import io.theurl.framework.utility.SnowflakeId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class Authlog extends AggregateRoot<Long> {
    /**
     * Initializes the aggregate with the given id.
     *
     * @param id the identifier of the aggregate
     */
    public Authlog(Long id) {
        super(id);
    }

    private Long userId;
    private String username;
    private String grantType;
    private String requestId;
    private String ipAddress;
    private String userAgent;
    private String referrer;
    private String appName;
    private String appVersion;
    private String osPlatform;
    private String source;
    private boolean success;
    private String remark;
    private LocalDateTime timestamp;

    public static Authlog create(String requestId, String username, boolean success) {
        var aggregate = new Authlog(SnowflakeId.getInstance().nextId());
        aggregate.setRequestId(requestId);
        aggregate.setUsername(username);
        aggregate.setSuccess(success);
        aggregate.setTimestamp(LocalDateTime.now());
        return aggregate;
    }
}

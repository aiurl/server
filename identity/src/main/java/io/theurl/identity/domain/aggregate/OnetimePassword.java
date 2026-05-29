package io.theurl.identity.domain.aggregate;

import io.theurl.framework.domain.AggregateRoot;
import io.theurl.framework.utility.SnowflakeId;
import io.theurl.identity.domain.event.OnetimePasswordCreatedEvent;

import java.time.LocalDateTime;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class OnetimePassword extends AggregateRoot<Long> {

    /**
     * Initializes the aggregate with the given id.
     *
     * @param id the identifier of the aggregate
     */
    public OnetimePassword(Long id) {
        super(id);
    }

    public OnetimePassword(Long id, String requestId, String recipient, String code) {
        this(id);
        this.requestId = requestId;
        this.recipient = recipient;
        this.code = code;
    }

    private String requestId;
    private String code;
    private String recipient;
    private LocalDateTime expiration;
    private LocalDateTime checked;
    private Integer duration;
    private String usage;

    public static OnetimePassword create(String requestId, String recipient, String code, Integer duration) {
        OnetimePassword otp = new OnetimePassword(SnowflakeId.getInstance().nextId(), requestId, recipient, code);
        otp.duration = duration;
        if (duration != null) {
            otp.expiration = LocalDateTime.now().plusMinutes(duration);
        }
        otp.raiseEvent(new OnetimePasswordCreatedEvent() {{
            setRequestId(requestId);
            setRecipient(recipient);
            setCode(code);
            setDuration(duration);
        }});
        return otp;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public LocalDateTime getChecked() {
        return checked;
    }

    public void setChecked(LocalDateTime checked) {
        this.checked = checked;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public void checkOff() {
        setChecked(LocalDateTime.now());
    }
}

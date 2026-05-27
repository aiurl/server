package io.theurl.identity.application.event;

import com.neroyun.mediator.Event;
import io.theurl.framework.domain.ApplicationEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAuthSuccessEvent extends ApplicationEvent implements Event {
    private final String grantType;
    private final Long userId;

    public UserAuthSuccessEvent(String grantType, Long userId) {
        this.grantType = grantType;
        this.userId = userId;
    }

    private String username;
    private LocalDateTime grantTime;

    @Getter
    private Map<String, Object> data = new HashMap<>();

    public void setData(String key, Object value) {
        if (data == null) {
            data = new HashMap<>();
        }

        if (value == null) {
            return;
        }

        this.data.put(key, value);
    }
}

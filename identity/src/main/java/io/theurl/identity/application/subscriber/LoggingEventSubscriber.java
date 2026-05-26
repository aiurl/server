package io.theurl.identity.application.subscriber;

import com.neroyun.mediator.Mediator;
import io.theurl.framework.core.BeanScope;
import io.theurl.identity.application.command.AuthlogCreateCommand;
import io.theurl.identity.application.event.UserAuthFailureEvent;
import io.theurl.identity.application.event.UserAuthSuccessEvent;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Scope(value = BeanScope.REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LoggingEventSubscriber {

    private final Logger LOGGER = LoggerFactory.getLogger(LoggingEventSubscriber.class);

    private final Mediator mediator;

    public LoggingEventSubscriber(Mediator mediator) {
        this.mediator = mediator;
    }

    @Async("taskExecutor")
    @EventListener
    public void handleUserAuthSuccess(UserAuthSuccessEvent event) {
        try {
            var request = getRequest();

            var command = new AuthlogCreateCommand();
            command.setUsername(event.getUsername());
            command.setUserId(event.getUserId());
            command.setGrantType(event.getGrantType());
            if (request != null) {
                command.setRequestId(request.getRequestId());
                command.setIpAddress(request.getRemoteAddr());
                command.setUserAgent(request.getHeader("User-Agent"));
                command.setReferrer(request.getHeader("Referer"));
                command.setAppName(request.getHeader("X-App-Name"));
                command.setAppVersion(request.getHeader("X-App-Version"));
                command.setOsPlatform(request.getHeader("X-OS-Platform"));
                command.setSource(request.getHeader("X-Source"));
            }
            command.setSuccess(true);
            command.setTimestamp(event.getGrantTime());
            mediator.sendAsync(command)
                    .join();
        } catch (Exception exception) {
            LOGGER.error("Failed to log authentication success event for user: {}, error: {}", event.getUsername(), exception.getMessage(), exception);
        }
    }

    @Async
    @EventListener
    public void handleUserAuthFailure(UserAuthFailureEvent event) {
        var request = getRequest();
        var command = new AuthlogCreateCommand();
        command.setUsername(event.getUsername());
        command.setUserId(event.getUserId());
        command.setGrantType(event.getGrantType());
        if (request != null) {
            command.setRequestId(request.getRequestId());
            command.setIpAddress(request.getRemoteAddr());
            command.setUserAgent(request.getHeader("User-Agent"));
            command.setReferrer(request.getHeader("Referer"));
            command.setAppName(request.getHeader("X-App-Name"));
            command.setAppVersion(request.getHeader("X-App-Version"));
            command.setOsPlatform(request.getHeader("X-OS-Platform"));
            command.setSource(request.getHeader("X-Source"));
        }
        command.setSuccess(false);
        command.setRemark(event.getError());
        command.setTimestamp(event.getGrantTime());
        mediator.sendAsync(command)
                .join();
    }

    private HttpServletRequest getRequest() {
        var request = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (request == null) {
            return null;
        }

        return request.getRequest();
    }
}

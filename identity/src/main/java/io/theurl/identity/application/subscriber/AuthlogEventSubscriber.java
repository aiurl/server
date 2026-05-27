package io.theurl.identity.application.subscriber;

import com.neroyun.mediator.Mediator;
import io.theurl.framework.core.BeanScope;
import io.theurl.framework.core.PriorityValueFinder;
import io.theurl.identity.application.command.AuthlogCreateCommand;
import io.theurl.identity.application.event.UserAuthFailureEvent;
import io.theurl.identity.application.event.UserAuthSuccessEvent;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Scope(BeanScope.PROTOTYPE)
public class AuthlogEventSubscriber {

    private static final String[] ipHeaders = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_CLIENT_IP",
        "HTTP_X_FORWARDED_FOR"
    };

    private final Logger LOGGER = LoggerFactory.getLogger(AuthlogEventSubscriber.class);

    private final Mediator mediator;

    public AuthlogEventSubscriber(Mediator mediator) {
        this.mediator = mediator;
    }

    @Async()
    @EventListener
    public void handleUserAuthSuccess(UserAuthSuccessEvent event) {
        try {
            var request = getRequest();

            var command = new AuthlogCreateCommand();
            command.setUsername(event.getUsername());
            command.setUserId(event.getUserId());
            command.setGrantType(event.getGrantType());
            if (request != null) {
                command.setRequestId(request.getSession().getId());
                command.setIpAddress(request.getRemoteAddr());
                command.setUserAgent(request.getHeader("User-Agent"));
                command.setReferrer(request.getHeader("Referer"));
                command.setAppName(request.getHeader("X-App-Name"));
                command.setAppVersion(request.getHeader("X-App-Version"));
                command.setOsPlatform(resolveOsPlatform(request.getHeader("User-Agent")));
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
        try {
            var request = getRequest();

            var command = new AuthlogCreateCommand();
            command.setUsername(event.getUsername());
            command.setUserId(event.getUserId());
            command.setGrantType(event.getGrantType());
            if (request != null) {
                command.setRequestId(request.getSession().getId());
                command.setIpAddress(getClientIp(request));
                command.setUserAgent(request.getHeader("User-Agent"));
                command.setReferrer(request.getHeader("Referer"));
                command.setAppName(request.getHeader("X-App-Name"));
                command.setAppVersion(request.getHeader("X-App-Version"));
                command.setOsPlatform(resolveOsPlatform(request.getHeader("User-Agent")));
                command.setSource(request.getHeader("X-Source"));
            }
            command.setSuccess(false);
            command.setRemark(event.getError());
            command.setTimestamp(event.getGrantTime());
            mediator.sendAsync(command)
                    .join();
        } catch (Exception exception) {
            LOGGER.error("Failed to log authentication failure event for user: {}, error: {}", event.getUsername(), exception.getMessage(), exception);
        }
    }

    private static String resolveOsPlatform(String userAgent) {
        // Implement OS platform resolution logic based on the user agent string

        try {
            var os = userAgent.replaceAll(".*(?<os>(?:Windows NT|Mac OS X|Android|iPhone OS|iPad OS|Linux|Ubuntu)[^;)]*).*", "$1");
            if (os.contains("Windows")) {
                return os.replace("Windows NT", "Windows");
            }
            if (os.contains("Mac OS X")) {
                return "macOS";
            }
            if (os.contains("Android")) {
                return "Android";
            }
            if (os.contains("iPad OS") || os.contains("iPhone OS")) {
                return "iOS";
            }
            if (os.contains("Linux")) {
                return "Linux";
            }

            return os;
        } catch (Exception exception) {
            return "Unknown";
        }
    }

    private HttpServletRequest getRequest() {
        var request = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (request == null) {
            return null;
        }

        return request.getRequest();
    }

    public static String getClientIp(HttpServletRequest request) {
        return PriorityValueFinder.find(queue -> {
            for (String header : ipHeaders) {
                var value = request.getHeader(header);
                if (value == null) {
                    continue;
                }
                queue.offer(value);
            }
        }, value -> !value.isEmpty(), "127.0.0.1");
    }
}

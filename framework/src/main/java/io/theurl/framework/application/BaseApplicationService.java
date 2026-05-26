package io.theurl.framework.application;

import com.neroyun.mediator.Mediator;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;

@Service
@RequestScope
public class BaseApplicationService implements ApplicationService {
    protected Mediator mediator;

    protected ThreadPoolTaskExecutor mediatorTaskExecutor;

    protected BaseApplicationService(ApplicationContext applicationContext) {
        mediator = applicationContext.getBean(Mediator.class);
        mediatorTaskExecutor = applicationContext.getBean(ThreadPoolTaskExecutor.class);
    }

    protected void handleException(Throwable throwable, Consumer<Throwable> consumer) {
        if (throwable instanceof CompletionException exception) {
            handleException(exception.getCause(), consumer);
        } else {
            consumer.accept(throwable);
        }
    }

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    protected Principal currentUser() {
        var request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getUserPrincipal();
    }
}

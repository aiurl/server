package io.theurl.framework.configure;

import com.neroyun.mediator.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@SuppressWarnings("rawtypes")
@Configuration
public class MediatorConfiguration {
    @Bean
    public Mediator mediator(ObjectProvider<Handler> handlers, ObjectProvider<Middleware> middlewares, ObjectProvider<Validator> validators) {
        return new PipelinedMediator()
            .use(() -> handlers.stream())
            .use(() -> middlewares.stream())
            .use(() -> validators.stream());
    }

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("mediator-");
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(32);
        executor.setQueueCapacity(200);
        executor.setTaskDecorator(copyRequestContextDecorator());
        executor.initialize();
        return executor;
    }

    private TaskDecorator copyRequestContextDecorator() {
        return runnable -> {
            RequestAttributes context = RequestContextHolder.getRequestAttributes();
            return () -> {
                try {
                    if (context != null) {
                        RequestContextHolder.setRequestAttributes(context);
                    }
                    runnable.run();
                } finally {
                    RequestContextHolder.resetRequestAttributes();
                }
            };
        };
    }
}

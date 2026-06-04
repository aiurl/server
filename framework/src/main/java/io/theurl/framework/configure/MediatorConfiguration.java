package io.theurl.framework.configure;

import com.neroyun.mediator.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("rawtypes")
@Configuration
public class MediatorConfiguration {
    @Bean
    public Mediator mediator(ObjectProvider<Handler> handlers, ObjectProvider<Middleware> middlewares, ObjectProvider<Validator> validators, ApplicationEventPublisher publisher) {
        return new PipelinedMediator()
            .use(() -> handlers.stream())
            .use(() -> middlewares.stream())
            .use(() -> validators.stream())
            .use(event -> CompletableFuture.runAsync(() -> publisher.publishEvent(event), taskExecutor().getThreadPoolExecutor()));
    }

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("mediator-");
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(32);
        executor.setQueueCapacity(200);
        executor.setTaskDecorator(new ContextCopyingDecorator());
        executor.initialize();
        return executor;
    }

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        multicaster.setTaskExecutor(taskExecutor());
        //multicaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return multicaster;
    }
}

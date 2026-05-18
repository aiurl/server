package io.theurl.message.configure;

import com.neroyun.mediator.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
public class MediatorConfiguration {
    @Bean
    public Mediator mediator(ObjectProvider<Handler> handlers, ObjectProvider<Middleware> middlewares, ObjectProvider<Validator> validators) {
        return new PipelinedMediator()
            .use(() -> handlers.stream())
            .use(() -> middlewares.stream())
            .use(() -> validators.stream())
            .use(Executors::newVirtualThreadPerTaskExecutor);
    }

}

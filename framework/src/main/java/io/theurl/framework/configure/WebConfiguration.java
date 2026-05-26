package io.theurl.framework.configure;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.RequestContextFilter;

@Configuration
public class WebConfiguration {
    @Bean
    public ServletListenerRegistrationBean<RequestContextListener> requestContextListenerRegistration() {
        ServletListenerRegistrationBean<RequestContextListener> registrationBean = new ServletListenerRegistrationBean<>();
        registrationBean.setListener(new RequestContextListener());
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RequestContextFilter> requestContextFilterRegistration() {
        RequestContextFilter filter = new RequestContextFilter();
        // Force the request context to pass down to asynchronous child threads
        filter.setThreadContextInheritable(true);

        FilterRegistrationBean<RequestContextFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}

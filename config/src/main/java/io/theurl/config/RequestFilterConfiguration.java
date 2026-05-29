package io.theurl.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestFilterConfiguration {

    @Bean
    public FilterRegistrationBean<LoggingRequestFilter> loggingFilterRegistration() {
        FilterRegistrationBean<LoggingRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoggingRequestFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}

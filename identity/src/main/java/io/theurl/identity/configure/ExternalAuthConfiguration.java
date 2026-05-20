package io.theurl.identity.configure;

import io.theurl.identity.external.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalAuthConfiguration {
    @Bean("microsoft")
    public ExternalAuthProvider microsoft() {
        return new MicrosoftAuthProvider();
    }

    @Bean("github")
    public ExternalAuthProvider github() {
        return new GithubAuthProvider();
    }
}

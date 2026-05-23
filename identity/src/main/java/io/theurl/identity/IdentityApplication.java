package io.theurl.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@ComponentScans({
    @ComponentScan("io.theurl.framework"),
    @ComponentScan("io.theurl.identity")
})
public class IdentityApplication {

    public static void main(String[] args) {

        SpringApplication.run(IdentityApplication.class, args);
    }

}

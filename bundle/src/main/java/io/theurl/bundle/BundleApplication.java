package io.theurl.bundle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BundleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BundleApplication.class, args);
    }

}

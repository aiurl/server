package io.theurl.identity.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

        @Value("${spring.data.redis.url}")
    private String redisUrl;

    @GetMapping("/test")
    public String test() {
        return "Redis URL: " + redisUrl;
    }
}

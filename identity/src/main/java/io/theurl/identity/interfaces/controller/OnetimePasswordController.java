package io.theurl.identity.interfaces.controller;

import io.theurl.identity.application.contract.OnetimePasswordApplicationService;
import io.theurl.identity.application.dto.OnetimePasswordSendRequestDto;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("otp")
public class OnetimePasswordController {
    private final OnetimePasswordApplicationService service;

    public OnetimePasswordController(OnetimePasswordApplicationService service) {
        this.service = service;
    }

    @PostMapping("send")
    public CompletableFuture<String> sendOtp(@RequestBody OnetimePasswordSendRequestDto request) {
        return service.sendAsync(request);
    }
}

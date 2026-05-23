package io.theurl.identity.application.contract;

import io.theurl.identity.interfaces.dto.TokenGrantRequestDto;
import io.theurl.identity.interfaces.dto.TokenGrantResponseDto;

import java.util.concurrent.CompletableFuture;

public interface AuthApplicationService {
    CompletableFuture<TokenGrantResponseDto> grant(TokenGrantRequestDto request);
}

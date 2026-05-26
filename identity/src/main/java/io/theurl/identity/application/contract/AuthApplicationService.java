package io.theurl.identity.application.contract;

import io.theurl.identity.application.dto.TokenGrantRequestDto;
import io.theurl.identity.application.dto.TokenGrantResponseDto;

import java.util.concurrent.CompletableFuture;

public interface AuthApplicationService {
    CompletableFuture<TokenGrantResponseDto> grant(TokenGrantRequestDto request);
}

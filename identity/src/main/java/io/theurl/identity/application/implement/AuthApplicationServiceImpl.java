package io.theurl.identity.application.implement;

import com.neroyun.mediator.Event;
import com.neroyun.mediator.internal.AggregateException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.theurl.framework.application.BaseApplicationService;
import io.theurl.framework.core.ObjectId;
import io.theurl.framework.security.*;
import io.theurl.framework.utility.Cryptography;
import io.theurl.identity.application.contract.AuthApplicationService;
import io.theurl.identity.application.event.UserAuthFailureEvent;
import io.theurl.identity.application.event.UserAuthSuccessEvent;
import io.theurl.identity.external.ExternalAuthProvider;
import io.theurl.identity.external.ExternalAuthResult;
import io.theurl.identity.interfaces.dto.TokenGrantRequestDto;
import io.theurl.identity.interfaces.dto.TokenGrantResponseDto;
import io.theurl.identity.persistence.model.UserAuthInfo;
import io.theurl.identity.persistence.query.OnetimePasswordDetailQuery;
import io.theurl.identity.persistence.query.UserAuthInfoQuery;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequestScope
@Service
public class AuthApplicationServiceImpl extends BaseApplicationService implements AuthApplicationService {

    @Value("${jwt.secret}")
    private String signingKey;

    @Value("${jwt.issuer}")
    private String issuer;

    private final ApplicationContext applicationContext;

    @Autowired
    public AuthApplicationServiceImpl(ApplicationContext applicationContext) {
        super(applicationContext);
        this.applicationContext = applicationContext;
    }

    @Override
    public CompletableFuture<TokenGrantResponseDto> grant(TokenGrantRequestDto request) {
        return CompletableFuture.supplyAsync(() -> {
            var events = new ArrayList<Event>();
            try {

                UserAuthInfoQuery query = switch (request.grantType().toLowerCase()) {
                    case null -> throw new IllegalArgumentException("Grant type is required");
                    case "" -> throw new IllegalArgumentException("Grant type is required");
                    case "password" -> {
                        if (request.username() == null || request.username().isEmpty()) {
                            throw new IllegalArgumentException("Username is required for username grant type");
                        }
                        yield new UserAuthInfoQuery("username", request.username());
                    }
                    case "email", "phone" ->
                        // For email and phone grant types, we should check OTP or other verification methods before querying user info.
                        checkCodeAsync(request).thenApply(_ -> new UserAuthInfoQuery(request.grantType(), request.username())).join();
                    case "github", "microsoft", "google", "facebook" ->
                        authWithExternalAsync(request.grantType(), request.username()).thenApply(userId -> new UserAuthInfoQuery(request.grantType(), userId)).join();
                    default -> throw new IllegalArgumentException("Unsupported grant type: " + request.grantType());
                };

                var userInfo = mediator.executeAsync(query).join();

                if (userInfo == null) {
                    throw new CredentialNotFoundException(request.username(), "Invalid username or password.");
                }

                if (userInfo.getLockedUntil().isAfter(LocalDateTime.now())) {
                    throw new AccountLockedException(request.username(), "Account is locked until " + userInfo.getLockedUntil());
                }

                if (request.grantType().equals("password")) {
                    var passwordHash = Cryptography.AES.encrypt(request.password(), userInfo.getPasswordSalt());
                    if (!passwordHash.equals(userInfo.getPasswordHash())) {
                        throw new CredentialIncorrectException("Invalid username or password.");
                    }
                }

                events.add(new UserAuthSuccessEvent(request.grantType(), userInfo.getId()));

                var jwtId = ObjectId.guid().toString();
                var iat = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
                var exp = LocalDateTime.now().plusHours(24).toEpochSecond(ZoneOffset.UTC);
                var accessToken = generateToken(jwtId, userInfo, iat, exp);

                return new TokenGrantResponseDto(accessToken, jwtId, "Bearer", 3600 * 24, iat, userInfo.getUsername(), userInfo.getId());

            } catch (Exception e) {
                var event = new UserAuthFailureEvent();

                handleException(e, ex -> {
                    switch (ex) {
                        case AccountLockedException exception:
                            event.setUserId(exception.getIdentity());
                            event.setGrantType(request.grantType());
                            event.setGrantTime(LocalDateTime.now());
                            event.setError(exception.getLocalizedMessage());
                            event.setData(Map.of("username", request.username() != null ? request.username() : "", "password", request.password(), "locked", "true"));
                            break;
                        case NoResultException ignored:
                            event.setUsername(request.username());
                            event.setGrantType(request.grantType());
                            event.setGrantTime(LocalDateTime.now());
                            event.setError(ex.getLocalizedMessage());
                            event.setData(Map.of("username", request.username()));
                            break;
                        case EntityNotFoundException ignored:
                            event.setUsername(request.username());
                            event.setGrantType(request.grantType());
                            event.setGrantTime(LocalDateTime.now());
                            event.setError(ex.getLocalizedMessage());
                            event.setData(Map.of("username", request.username()));
                            break;
                        case AccountNotFoundException ignored:
                            event.setUsername(request.username());
                            event.setGrantType(request.grantType());
                            event.setGrantTime(LocalDateTime.now());
                            event.setError(ex.getLocalizedMessage());
                            event.setData(Map.of("username", request.username()));
                            break;
                        case CredentialException exception:
                            event.setUsername(request.username());
                            event.setGrantType(request.grantType());
                            event.setGrantTime(LocalDateTime.now());
                            event.setError(exception.getLocalizedMessage());
                            event.setData(Map.of("username", request.username(), "password", request.password()));
                            break;
                        default:
                            break;
                    }
                });
                log.error("Error while processing request", e);
                throw new AggregateException(List.of(e));
            } finally {
                if (!events.isEmpty()) {
                    events.parallelStream().forEach(mediator::publishAsync);
                }
            }
        }, mediatorTaskExecutor.getThreadPoolExecutor());
    }

    CompletableFuture<Void> checkCodeAsync(TokenGrantRequestDto request) {
        return mediator.executeAsync(new OnetimePasswordDetailQuery(request.requestId())).thenAccept(otp -> {
            if (otp == null) {
                throw new CredentialIncorrectException("Invalid verify code.");
            }
            if (otp.getExpiration().isBefore(LocalDateTime.now())) {
                throw new CredentialIncorrectException("Verify code has expired.");
            }
            if (otp.getChecked() != null) {
                throw new CredentialIncorrectException("Verify code has already been used.");
            }
            if (!otp.getRecipient().equals(request.username())) {
                throw new CredentialIncorrectException("Invalid verify code recipient.");
            }
            if (otp.getCode() == null || !otp.getCode().equals(request.password())) {
                throw new CredentialIncorrectException("Invalid verify code.");
            }
        });
    }

    CompletableFuture<String> authWithExternalAsync(String grantType, String username) {
        var provider = applicationContext.getBean(("external-auth-provider-" + grantType).toLowerCase(), ExternalAuthProvider.class);
        // Here we should check if the external auth result is linked to an internal user account, and return the user ID.
        // For simplicity, we just return a dummy user ID.
        return provider.authenticateAsync(username).thenApply(ExternalAuthResult::getId);
    }

    private String generateToken(String id, UserAuthInfo user, long issuedAt, long expiresAt) {
        Assert.notNull(user, "user cannot be null");
        //var signingKey = environment.getProperty("JwtAuthenticationOptions.SigningKey");
        Assert.notNull(signingKey, "SigningKey cannot be null");

        var builder = Jwts.builder();
        builder.subject(String.valueOf(user.getId())).id(id).issuer(issuer).issuedAt(new Date(issuedAt)).expiration(new Date(expiresAt)) // 24小时后过期
               .claim("name", user.getUsername());
        builder.signWith(Keys.hmacShaKeyFor(signingKey.getBytes()));
        return builder.compact();
    }
}

package io.theurl.identity.application.implement;

import com.neroyun.mediator.Event;
import com.neroyun.mediator.internal.AggregateException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.theurl.framework.application.BaseApplicationService;
import io.theurl.framework.core.ObjectId;
import io.theurl.framework.security.*;
import io.theurl.framework.utility.Cryptography;
import io.theurl.framework.utility.DateTimeUtility;
import io.theurl.framework.utility.RegexUtility;
import io.theurl.identity.application.contract.AuthApplicationService;
import io.theurl.identity.application.event.TokenGrantedEvent;
import io.theurl.identity.application.event.TokenRefreshedEvent;
import io.theurl.identity.application.event.UserAuthFailureEvent;
import io.theurl.identity.application.event.UserAuthSuccessEvent;
import io.theurl.identity.domain.enums.TokenStatus;
import io.theurl.identity.external.ExternalAuthProvider;
import io.theurl.identity.external.ExternalAuthResult;
import io.theurl.identity.application.dto.TokenGrantRequestDto;
import io.theurl.identity.application.dto.TokenGrantResponseDto;
import io.theurl.identity.persistence.model.UserAuthInfo;
import io.theurl.identity.persistence.query.OnetimePasswordDetailQuery;
import io.theurl.identity.persistence.query.TokenDetailQuery;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequestScope
@Service
public class AuthApplicationServiceImpl extends BaseApplicationService implements AuthApplicationService {

    @Value("${jwt.secret}")
    private String signingKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Autowired
    public AuthApplicationServiceImpl(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public CompletableFuture<TokenGrantResponseDto> grant(TokenGrantRequestDto request) {
        checkRequest(request);
        var events = new ArrayList<Event>();
        try {
            UserAuthInfoQuery query = switch (request.grantType().toLowerCase()) {
                case "password" -> new UserAuthInfoQuery("username", request.username());
                case "email",
                     "phone" -> // For email and phone grant types, we should check OTP or other verification methods before querying user info.
                    checkCodeAsync(request).thenApply(_ -> new UserAuthInfoQuery(request.grantType(), request.username())).join();
                case "github", "microsoft", "google", "facebook" ->
                    authWithExternalAsync(request.grantType(), request.username()).thenApply(userId -> new UserAuthInfoQuery(request.grantType(), userId)).join();
                case "refresh_token" ->
                    refreshToken(request.username()).thenApply(userId -> new UserAuthInfoQuery("id", String.valueOf(userId))).join();
                default -> throw new IllegalArgumentException("Unsupported grant type: " + request.grantType());
            };

            var userInfo = mediator.executeAsync(query).join();

            if (userInfo == null) {
                throw new CredentialNotFoundException(null, "Invalid username or password.") {
                    @Override
                    public Map<String, Object> getDetails() {
                        return Map.of("username", request.username() != null ? request.username() : "");
                    }
                };
            }

            if (userInfo.getLockedUntil() != null && userInfo.getLockedUntil().isAfter(LocalDateTime.now())) {
                throw new AccountLockedException(userInfo.getId(), "Account is locked until " + userInfo.getLockedUntil()) {
                    @Override
                    public Map<String, Object> getDetails() {
                        return Map.of("username", userInfo.getUsername());
                    }
                };
            }

            if (request.grantType().equals("password")) {
                var passwordHash = Cryptography.AES.encrypt(request.password(), userInfo.getPasswordSalt());
                if (!passwordHash.equals(userInfo.getPasswordHash())) {
                    throw new CredentialIncorrectException(userInfo.getId(), "Invalid username or password.") {
                        @Override
                        public Map<String, Object> getDetails() {
                            return Map.of("username", userInfo.getUsername());
                        }
                    };
                }
            }

            var jwtId = ObjectId.guid().toString();
            var iat = LocalDateTime.now();//.toEpochSecond(ZoneOffset.UTC);
            var exp = LocalDateTime.now().plusHours(24);//.toEpochSecond(ZoneOffset.UTC);
            var accessToken = generateToken(jwtId, userInfo, DateTimeUtility.toDate(iat), DateTimeUtility.toDate(exp));

            events.add(new UserAuthSuccessEvent(request.grantType(), userInfo.getId()) {{
                setGrantTime(iat);
            }});

            events.add(new TokenGrantedEvent(jwtId, userInfo.getId(), accessToken) {{
                setIssuedAt(iat);
                setExpiresAt(exp);
            }});

            if (request.grantType().equals("refresh_token")) {
                events.add(new TokenRefreshedEvent(request.username()));
            }

            var result = new TokenGrantResponseDto(accessToken, jwtId, "Bearer", 3600 * 24, iat.toEpochSecond(ZoneOffset.UTC), userInfo.getUsername(), userInfo.getId());

            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            var event = new UserAuthFailureEvent();
            event.setGrantType(request.grantType());
            event.setGrantTime(LocalDateTime.now());

            handleException(e, ex -> {
                switch (ex) {
                    case AccountLockedException exception:
                        event.setUserId((Long) exception.getIdentity());
                        event.setUsername((String) exception.getDetails().get("username"));
                        event.setError(exception.getLocalizedMessage());
                        break;
                    case NoResultException ignored:
                        event.setUsername(request.username());
                        event.setError(ex.getLocalizedMessage());
                        break;
                    case EntityNotFoundException ignored:
                        event.setUsername(request.username());
                        event.setError(ex.getLocalizedMessage());
                        break;
                    case AccountNotFoundException ignored:
                        event.setUsername(request.username());
                        event.setError(ex.getLocalizedMessage());
                        break;
                    case CredentialException exception:
                        if (exception.getCredential() instanceof Long userId) {
                            event.setUserId(userId);
                        }
                        event.setUsername((String) exception.getDetails().get("username"));
                        event.setError(exception.getLocalizedMessage());
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

    CompletableFuture<Long> refreshToken(String jti) {
        var query = new TokenDetailQuery(jti);
        return mediator.executeAsync(query)
                       .thenApply(tokenDetail -> {
                           if (tokenDetail == null) {
                               throw new CredentialNotFoundException(null, "Invalid refresh token.") {
                                   @Override
                                   public Map<String, Object> getDetails() {
                                       return Map.of("jti", jti);
                                   }
                               };
                           }

                           if (TokenStatus.valueOf(tokenDetail.getStatus()) == TokenStatus.REFRESHED) {
                               throw new CredentialNotFoundException(null, "Refresh token has been revoked.") {
                                   @Override
                                   public Map<String, Object> getDetails() {
                                       return Map.of("jti", jti);
                                   }
                               };
                           }

                           return tokenDetail.getSubject();
                       });
    }

    private String generateToken(String id, UserAuthInfo user, Date issuedAt, Date expiresAt) {
        Assert.notNull(user, "user cannot be null");
        //var signingKey = environment.getProperty("JwtAuthenticationOptions.SigningKey");
        Assert.notNull(signingKey, "SigningKey cannot be null");

        var builder = Jwts.builder();
        builder.subject(String.valueOf(user.getId())).id(id)
               .issuer(issuer)
               .issuedAt(issuedAt)
               .expiration(expiresAt)
               .claim("name", user.getUsername());

        if (user.getRoles() != null) {
            for (var role : user.getRoles()) {
                builder.claim("role", role);
            }
        }

        builder.signWith(Keys.hmacShaKeyFor(signingKey.getBytes()));
        return builder.compact();
    }

    private void checkRequest(TokenGrantRequestDto request) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(request.grantType(), "grantType cannot be null");

        switch (request.grantType()) {
            case "password", "username":
                Assert.notNull(request.username(), "Username cannot be null");
                Assert.notNull(request.password(), "Password cannot be null");
                break;
            case "phone":
                Assert.notNull(request.username(), "Phone number cannot be null");
                Assert.isTrue(request.username().matches(RegexUtility.PHONE_REGEX), "Invalid phone number format");
                Assert.notNull(request.password(), "Onetime password cannot be null");
                Assert.notNull(request.requestId(), "Request ID cannot be null for OTP grant type");
                break;
            case "email":
                Assert.notNull(request.username(), "Email cannot be null");
                Assert.isTrue(request.username().matches(RegexUtility.EMAIL_REGEX), "Invalid email format");
                Assert.notNull(request.password(), "Onetime password cannot be null");
                Assert.notNull(request.requestId(), "Request ID cannot be null for OTP grant type");
                break;
            case "github", "microsoft", "google", "facebook":
                Assert.notNull(request.username(), "OAuth code cannot be null");
                break;
            case "refresh_token":
                Assert.notNull(request.username(), "Refresh token cannot be null");
                break;
            default:
                throw new IllegalArgumentException("Unsupported grant type: " + request.grantType());
        }
    }
}

package io.theurl.identity.application.implement;

import io.theurl.framework.application.BaseApplicationService;
import io.theurl.framework.security.CredentialIncorrectException;
import io.theurl.framework.utility.Cryptography;
import io.theurl.identity.application.command.UserAuthorityCreateCommand;
import io.theurl.identity.application.command.UserCreateCommand;
import io.theurl.identity.application.command.UserPasswordChangeCommand;
import io.theurl.identity.application.command.UserUpdateCommand;
import io.theurl.identity.application.contract.UserApplicationService;
import io.theurl.identity.application.dto.UserCreateRequestDto;
import io.theurl.identity.application.dto.UserProfileResponseDto;
import io.theurl.identity.application.dto.UserUpdateRequestDto;
import io.theurl.identity.external.ExternalAuthProvider;
import io.theurl.identity.persistence.query.OnetimePasswordDetailQuery;
import io.theurl.identity.persistence.query.UserAuthInfoQuery;
import io.theurl.identity.persistence.query.UserDetailQuery;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Service
@RequestScope
public class UserApplicationServiceImpl extends BaseApplicationService implements UserApplicationService {

    private final ModelMapper mapper;

    public UserApplicationServiceImpl(ApplicationContext applicationContext, ModelMapper mapper) {
        super(applicationContext);
        this.mapper = mapper;
    }

    @Override
    public CompletableFuture<Void> createAsync(UserCreateRequestDto user) {
        var command = mapper.map(user, UserCreateCommand.class);
        return mediator.sendAsync(command);
    }

    @Override
    public CompletableFuture<UserProfileResponseDto> getProfileAsync() {
        var query = new UserDetailQuery(currentUserId());
        return mediator.executeAsync(query)
                       .thenApply(userDetail -> mapper.map(userDetail, UserProfileResponseDto.class));
    }

    @Override
    public CompletableFuture<Void> changePasswordAsync(String oldPassword, String newPassword) {
        mediator.executeAsync(new UserAuthInfoQuery("id", String.valueOf(currentUserId())))
                .thenAccept(userDetail -> {
                    if (userDetail == null) {
                        throw new IllegalStateException("User not found.");
                    }
                    try {
                        var oldPasswordHash = Cryptography.AES.encrypt(oldPassword, userDetail.getPasswordSalt());
                        if (!oldPasswordHash.equals(userDetail.getPasswordHash())) {
                            throw new CredentialIncorrectException("Old password is incorrect.");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to encrypt old password", e);
                    }
                })
                .join();

        var command = new UserPasswordChangeCommand(currentUserId(), newPassword, "change");
        return mediator.sendAsync(command);
    }

    @Override
    public CompletableFuture<Void> changeEmailAsync(UserUpdateRequestDto data) {
        checkCodeAsync(data.getRequestId(), data.getPhone(), data.getCode())
            .join();
        var command = new UserUpdateCommand(currentUserId());
        command.getModifications().put("email", data.getEmail());
        return mediator.sendAsync(command);
    }

    @Override
    public CompletableFuture<Void> changePhoneAsync(UserUpdateRequestDto data) {
        checkCodeAsync(data.getRequestId(), data.getPhone(), data.getCode())
            .join();

        var command = new UserUpdateCommand(currentUserId());
        command.getModifications().put("phone", data.getPhone());
        return mediator.sendAsync(command);
    }

    @Override
    public CompletableFuture<Void> changeNicknameAsync(String nickname) {
        var command = new UserUpdateCommand(currentUserId());
        command.getModifications().put("nickname", nickname);
        return mediator.sendAsync(command);
    }

    @Override
    public CompletableFuture<Void> connectAuthorityAsync(String provider, String code) {
        provider = provider.toLowerCase(Locale.ROOT);

        var authProvider = applicationContext.getBean("external-auth-provider-" + provider, ExternalAuthProvider.class);

        var externalAuthResult = authProvider.authenticateAsync(code)
                                             .join();

        var command = new UserAuthorityCreateCommand(currentUserId(), provider, externalAuthResult.getId(), externalAuthResult.getNickname());
        return mediator.sendAsync(command);
    }

    @Override
    public CompletableFuture<Void> removeAuthorityAsync(String provider, String openId) {
        return null;
    }

    CompletableFuture<Void> checkCodeAsync(String requestId, String recipient, String code) {
        return mediator.executeAsync(new OnetimePasswordDetailQuery(requestId)).thenAccept(otp -> {
            if (otp == null) {
                throw new CredentialIncorrectException("Invalid verify code.");
            }
            if (otp.getExpiration().isBefore(LocalDateTime.now())) {
                throw new CredentialIncorrectException("Verify code has expired.");
            }
            if (otp.getChecked() != null) {
                throw new CredentialIncorrectException("Verify code has already been used.");
            }
            if (!otp.getRecipient().equals(recipient)) {
                throw new CredentialIncorrectException("Invalid verify code recipient.");
            }
            if (otp.getCode() == null || !otp.getCode().equals(code)) {
                throw new CredentialIncorrectException("Invalid verify code.");
            }
        });
    }
}

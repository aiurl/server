package io.theurl.identity.application.implement;

import io.theurl.framework.application.BaseApplicationService;
import io.theurl.framework.core.ObjectId;
import io.theurl.framework.utility.RandomUtility;
import io.theurl.framework.utility.RegexUtility;
import io.theurl.framework.security.CredentialNotFoundException;
import io.theurl.identity.application.command.OnetimePasswordCreateCommand;
import io.theurl.identity.application.contract.OnetimePasswordApplicationService;
import io.theurl.identity.persistence.query.UserAuthInfoQuery;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequestScope
public class OnetimePasswordApplicationServiceImpl extends BaseApplicationService implements OnetimePasswordApplicationService {
    public OnetimePasswordApplicationServiceImpl(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public CompletableFuture<String> sendAsync(String recipient, String usage) {
        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("Recipient cannot be null or blank");
        }

        boolean isEmail = RegexUtility.isEmail(recipient);
        boolean isPhone = RegexUtility.isPhone(recipient);

        if (!isEmail && !isPhone) {
            throw new IllegalArgumentException("Email or Phone number is invalid");
        }

        if (Objects.equals(usage, "authentication")) {
            UserAuthInfoQuery query;
            if (isEmail) {
                query = new UserAuthInfoQuery("email", recipient);
            } else {
                query = new UserAuthInfoQuery("phone", recipient);
            }

            var user = mediator.executeAsync(query).join();
            if (user == null) {
                throw new CredentialNotFoundException(recipient, recipient + " not registered.");
            }
        }

        var requestId = ObjectId.guid().toString();
        var code = RandomUtility.randomString(6, RandomUtility.Mode.NUMERIC);

        var command = new OnetimePasswordCreateCommand(requestId, recipient, code);
        command.setUsage(usage);
        command.setDuration(15);
        return mediator.sendAsync(command).thenApply(_ -> requestId);
    }
}

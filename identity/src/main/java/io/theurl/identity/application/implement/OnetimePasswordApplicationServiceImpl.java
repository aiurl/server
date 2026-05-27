package io.theurl.identity.application.implement;

import io.theurl.framework.application.BaseApplicationService;
import io.theurl.framework.core.ObjectId;
import io.theurl.framework.utility.RandomUtility;
import io.theurl.framework.utility.RegexUtility;
import io.theurl.identity.application.command.OnetimePasswordCreateCommand;
import io.theurl.identity.application.contract.OnetimePasswordApplicationService;
import io.theurl.identity.application.dto.OnetimePasswordSendRequestDto;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.concurrent.CompletableFuture;

@Service
@RequestScope
public class OnetimePasswordApplicationServiceImpl extends BaseApplicationService implements OnetimePasswordApplicationService {
    public OnetimePasswordApplicationServiceImpl(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public CompletableFuture<String> sendAsync(OnetimePasswordSendRequestDto request) {

        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (request.recipient() == null || request.recipient().isBlank()) {
            throw new IllegalArgumentException("Recipient cannot be null or blank");
        }

        if (!RegexUtility.isEmail(request.recipient()) && !RegexUtility.isPhone(request.recipient())) {
            throw new IllegalArgumentException("Email or Phone number is invalid");
        }

        var requestId = ObjectId.guid().toString();
        var code = RandomUtility.randomString(6, RandomUtility.Mode.NUMERIC);

        var command = new OnetimePasswordCreateCommand(requestId, request.recipient(), code);
        return mediator.sendAsync(command).thenApply(_ -> requestId);
    }
}

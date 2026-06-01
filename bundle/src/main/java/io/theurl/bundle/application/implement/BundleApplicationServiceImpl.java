package io.theurl.bundle.application.implement;

import io.theurl.bundle.application.command.BundleCreateCommand;
import io.theurl.bundle.application.contract.BundleApplicationService;
import io.theurl.bundle.application.dto.BundleCreateDto;
import io.theurl.bundle.application.dto.BundleUpdateDto;
import io.theurl.framework.application.BaseApplicationService;
import io.theurl.framework.utility.ShortUniqueId;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequestScope
public class BundleApplicationServiceImpl extends BaseApplicationService implements BundleApplicationService {

    private final ModelMapper mapper;

    public BundleApplicationServiceImpl(ApplicationContext applicationContext, ModelMapper mapper) {
        super(applicationContext);
        this.mapper = mapper;
    }

    @Override
    public CompletableFuture<String> createAsync(BundleCreateDto data) {
        if (data.getVanity() == null || data.getVanity().isEmpty()) {
            var uuid = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
            long number = 0;
            for (int i = 0; i < 8 && i < uuid.length; i++) {
                number = (number << 8) | (uuid[i] & 0xFF);
            }

            var shortId = ShortUniqueId.getDefault().encode(number);
            data.setVanity(shortId);
        }

        var command = new BundleCreateCommand(data.getType(), data.getVanity());
        command.setName(data.getName());
        command.setDescription(data.getDescription());
        command.setImage(data.getImage());

        return mediator.sendAsync(command)
                       .thenApply(_ -> command.getVanity());
    }

    @Override
    public CompletableFuture<Void> updateAsync(String vanity, BundleUpdateDto data) {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteAsync(String vanity) {
        return null;
    }
}

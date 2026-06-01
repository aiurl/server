package io.theurl.bundle.application.implement;

import com.neroyun.mediator.Event;
import io.theurl.bundle.application.command.BundleCreateCommand;
import io.theurl.bundle.application.command.BundleDeleteCommand;
import io.theurl.bundle.application.command.BundleUpdateCommand;
import io.theurl.bundle.application.contract.BundleApplicationService;
import io.theurl.bundle.application.dto.BundleCreateDto;
import io.theurl.bundle.application.dto.BundleItemEditDto;
import io.theurl.bundle.application.dto.BundleUpdateDto;
import io.theurl.framework.application.BaseApplicationService;
import io.theurl.framework.utility.ShortUniqueId;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

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
        if (!data.isShared()) {
            command.setOwnerId(currentUserId());
            command.setOwnerName(currentUsername());
        }

        return mediator.sendAsync(command, context -> {
                           context.subscribe(new Flow.Subscriber<>() {
                               @Override
                               public void onSubscribe(Flow.Subscription subscription) {
                                   subscription.request(Long.MAX_VALUE);
                               }

                               @Override
                               public void onNext(Object item) {
                                   if (item instanceof List<?> events) {
                                       events.parallelStream()
                                             .forEach(event -> mediator.publishAsync((Event) event));
                                   }
                               }

                               @Override
                               public void onError(Throwable throwable) {

                               }

                               @Override
                               public void onComplete() {

                               }
                           });
                       })
                       .thenApply(_ -> command.getVanity());
    }

    @Override
    public CompletableFuture<Void> updateAsync(String vanity, BundleUpdateDto data) {
        var command = new BundleUpdateCommand(vanity);
        mapper.map(data, command);
        return mediator.sendAsync(command);
    }

    @Override
    public CompletableFuture<Void> deleteAsync(String vanity) {
        var command = new BundleDeleteCommand(vanity);
        return mediator.sendAsync(command, context -> {
            context.subscribe(new Flow.Subscriber<>() {
                @Override
                public void onSubscribe(Flow.Subscription subscription) {
                    subscription.request(Long.MAX_VALUE);
                }

                @Override
                public void onNext(Object item) {
                    if (item instanceof List<?> events) {
                        events.parallelStream()
                              .forEach(event -> mediator.publishAsync((Event) event));
                    }
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }
            });
        });
    }

    @Override
    public CompletableFuture<Void> appendItemAsync(String vanity, BundleItemEditDto data) {
        return null;
    }

    @Override
    public CompletableFuture<Void> updateItemAsync(String vanity, long itemId, BundleItemEditDto data) {
        return null;
    }

    @Override
    public CompletableFuture<Void> removeItemAsync(String vanity, long itemId) {
        return null;
    }
}

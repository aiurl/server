package io.theurl.bundle.application.implement;

import com.neroyun.mediator.Event;
import io.theurl.bundle.application.command.*;
import io.theurl.bundle.application.contract.BundleApplicationService;
import io.theurl.bundle.application.dto.*;
import io.theurl.bundle.persistence.query.BundleCountQuery;
import io.theurl.bundle.persistence.query.BundleItemCountQuery;
import io.theurl.bundle.persistence.query.BundleItemListQuery;
import io.theurl.bundle.persistence.query.BundleListQuery;
import io.theurl.framework.application.BaseApplicationService;
import io.theurl.framework.utility.ShortUniqueId;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
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
        var command = new BundleItemAppendCommand(vanity);
        mapper.map(data, command);
        return mediator.sendAsync(command);
    }

    @Override
    public CompletableFuture<Void> updateItemAsync(String vanity, long itemId, BundleItemEditDto data) {
        var command = new BundleItemUpdateCommand(vanity, itemId);
        mapper.map(data, command);
        return mediator.sendAsync(command);
    }

    @Override
    public CompletableFuture<Void> removeItemAsync(String vanity, long itemId) {
        var command = new BundleItemRemoveCommand(vanity, itemId);
        return mediator.sendAsync(command);
    }

    @Override
    public CompletableFuture<List<BundleListDto>> searchAsync(Map<String, Object> criteria, int from, int size) {
        if (criteria.getOrDefault("owned", false).equals(true)) {
            criteria.put("ownerId", currentUserId());
        }
        var query = new BundleListQuery(criteria, from, size);
        return mediator.executeAsync(query)
                       .thenApply(models -> {
                           return models.stream()
                                        .map(model -> mapper.map(model, BundleListDto.class))
                                        .toList();
                       });
    }

    @Override
    public CompletableFuture<Integer> countAsync(Map<String, Object> criteria) {
        var query = new BundleCountQuery(criteria);
        return mediator.executeAsync(query);
    }

    @Override
    public CompletableFuture<List<BundleItemListDto>> searchItemsAsync(String vanity, Map<String, Object> criteria, int from, int size) {
        var query = new BundleItemListQuery(vanity, criteria, from, size);
        return mediator.executeAsync(query)
                       .thenApply(models -> {
                           return models.stream()
                                        .map(model -> mapper.map(model, BundleItemListDto.class))
                                        .toList();
                       });
    }

    @Override
    public CompletableFuture<Integer> countItemsAsync(String vanity, Map<String, Object> criteria) {
        var query = new BundleItemCountQuery(vanity, criteria);
        return mediator.executeAsync(query);
    }
}

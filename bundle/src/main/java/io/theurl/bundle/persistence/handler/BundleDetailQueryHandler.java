package io.theurl.bundle.persistence.handler;

import com.neroyun.mediator.Handler;
import io.theurl.bundle.persistence.model.BundleDetailModel;
import io.theurl.bundle.persistence.query.BundleDetailQuery;
import io.theurl.bundle.persistence.repository.JpaBundleRepository;
import io.theurl.framework.core.BeanScope;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Scope(BeanScope.PROTOTYPE)
public class BundleDetailQueryHandler implements Handler<BundleDetailQuery, BundleDetailModel> {

    private final JpaBundleRepository repository;
    private final ModelMapper mapper;

    public BundleDetailQueryHandler(JpaBundleRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CompletableFuture<BundleDetailModel> handleAsync(BundleDetailQuery message) {
        var entity = repository.findByVanity(message.vanity())
                               .orElse(null);
        if (entity == null || entity.isDeleted()) {
            throw new EntityNotFoundException("Bundle not found with vanity: " + message.vanity());
        }
        
        var detail = mapper.map(entity, BundleDetailModel.class);
        return CompletableFuture.completedFuture(detail);
    }
}

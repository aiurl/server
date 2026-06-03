package io.theurl.bundle.persistence.repository;

import io.theurl.bundle.domain.repository.BundleRepository;
import io.theurl.bundle.persistence.entity.Bundle;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class BundleRepositoryImpl implements BundleRepository {
    private final JpaBundleRepository repository;
    private final ModelMapper mapper;

    public BundleRepositoryImpl(JpaBundleRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(io.theurl.bundle.domain.aggregate.Bundle bundle, long operatorId) {
        var entity = repository.findById(bundle.getId())
                               .orElse(null);
        if (entity == null) {
            entity = mapper.map(bundle, Bundle.class);
            entity.setCreatedBy(operatorId);
            entity.setUpdatedBy(operatorId);
            entity.setCreatedAt(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());
        } else if (bundle.isDeleted()) {
            entity.setDeleted(true);
            entity.setDeletedBy(operatorId);
            entity.setDeletedAt(LocalDateTime.now());
        } else {
            mapper.map(bundle, entity);
            entity.setUpdatedBy(operatorId);
            entity.setUpdatedAt(LocalDateTime.now());
        }

        repository.save(entity);
    }

    @Override
    public io.theurl.bundle.domain.aggregate.Bundle findById(Long id) {
        var entity = repository.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        return mapper.map(entity, io.theurl.bundle.domain.aggregate.Bundle.class);
    }

    @Override
    public io.theurl.bundle.domain.aggregate.Bundle findByVanity(String vanity) {
        var entity = repository.findByVanity(vanity)
                               .orElse(null);
        if (entity == null || entity.isDeleted()) {
            return null;
        }
        return mapper.map(entity, io.theurl.bundle.domain.aggregate.Bundle.class);
    }
}

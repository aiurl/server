package io.theurl.bundle.persistence.repository;

import io.theurl.bundle.domain.aggregate.Bundle;
import io.theurl.bundle.domain.repository.BundleRepository;
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
    public void save(Bundle bundle, long operatorId) {
        var entity = repository.findById(bundle.getId())
                               .orElse(null);
        if (entity == null) {
            entity = mapper.map(bundle, io.theurl.bundle.persistence.entity.Bundle.class);
            entity.setCreatedBy(operatorId);
        } else if (bundle.isDeleted()) {
            entity.setDeleted(true);
            entity.setDeletedBy(operatorId);
            entity.setDeletedAt(LocalDateTime.now());
        } else {
            mapper.map(bundle, entity);
            entity.setUpdatedBy(operatorId);
        }

        repository.save(entity);
    }

    @Override
    public Bundle findById(Long id) {
        var entity = repository.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        return mapper.map(entity, Bundle.class);
    }

    @Override
    public Bundle findByVanity(String vanity) {
        var entity = repository.findByVanity(vanity)
                               .orElse(null);
        if (entity == null || entity.isDeleted()) {
            return null;
        }
        return mapper.map(entity, Bundle.class);
    }
}

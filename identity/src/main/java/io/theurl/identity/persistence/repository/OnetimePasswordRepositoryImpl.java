package io.theurl.identity.persistence.repository;

import io.theurl.identity.domain.aggregate.OnetimePassword;
import io.theurl.identity.domain.repository.OnetimePasswordRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OnetimePasswordRepositoryImpl implements OnetimePasswordRepository {

    private final JpaOnetimePasswordRepository repository;
    private final ModelMapper mapper;

    public OnetimePasswordRepositoryImpl(JpaOnetimePasswordRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(OnetimePassword aggregate) {
        var entity = mapper.map(aggregate, io.theurl.identity.persistence.entity.OnetimePassword.class);
        repository.save(entity);
    }

    @Override
    public OnetimePassword findById(Long id) {
        var entity = repository.findById(id).orElse(null);
        return mapper.map(entity, OnetimePassword.class);
    }

    @Override
    public OnetimePassword findByRequestId(String requestId) {
        var entity = repository.findByRequestId(requestId).orElse(null);
        return mapper.map(entity, OnetimePassword.class);
    }
}

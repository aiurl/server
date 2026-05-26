package io.theurl.identity.persistence.repository;

import io.theurl.identity.domain.aggregate.Authlog;
import io.theurl.identity.domain.repository.AuthlogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AuthlogRepositoryImpl implements AuthlogRepository {

    private final JpaAuthlogRepository repository;
    private final ModelMapper mapper;

    public AuthlogRepositoryImpl(JpaAuthlogRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(Authlog authlog) {
        var entity = mapper.map(authlog, io.theurl.identity.persistence.entity.Authlog.class);
        repository.save(entity);
    }
}

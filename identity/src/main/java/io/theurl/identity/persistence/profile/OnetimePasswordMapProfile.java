package io.theurl.identity.persistence.profile;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OnetimePasswordMapProfile {
    @Autowired
    private ModelMapper mapper;

    @PostConstruct
    public void configure() {
        Provider<io.theurl.identity.domain.aggregate.OnetimePassword> provider = request -> {
            var source = (io.theurl.identity.persistence.entity.OnetimePassword) request.getSource();
            return new io.theurl.identity.domain.aggregate.OnetimePassword(source.getId());
        };

        mapper.createTypeMap(io.theurl.identity.persistence.entity.OnetimePassword.class, io.theurl.identity.domain.aggregate.OnetimePassword.class)
              .setProvider(provider);
    }
}

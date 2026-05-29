package io.theurl.bundle.persistence.profile;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BundleExtendMapProfile {

    @Autowired
    private ModelMapper mapper;

    @PostConstruct
    public void configure() {

        Provider<io.theurl.bundle.domain.aggregate.BundleExtend> provider = request -> {
            var source = request.getSource();

            Long id;

            if (source instanceof io.theurl.bundle.domain.aggregate.BundleExtend entity) {
                id = entity.getId();
            } else {
                try {
                    var field = source.getClass().getDeclaredField("id");
                    field.setAccessible(true);
                    id = (Long) field.get(source);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException("Failed to create provider for BundleExtend", e);
                }
            }

            return new io.theurl.bundle.domain.aggregate.BundleExtend(id);
        };

        mapper.createTypeMap(io.theurl.bundle.persistence.entity.BundleExtend.class, io.theurl.bundle.domain.aggregate.BundleExtend.class)
              .setProvider(provider);

    }
}

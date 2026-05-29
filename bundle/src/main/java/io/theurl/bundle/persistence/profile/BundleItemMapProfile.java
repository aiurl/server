package io.theurl.bundle.persistence.profile;

import io.theurl.bundle.domain.aggregate.BundleItem;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BundleItemMapProfile {

    @Autowired
    private ModelMapper mapper;

    @PostConstruct
    public void configure() {
        Provider<BundleItem> provider = request -> {
            var source = request.getSource();

            long id;

            if (source instanceof io.theurl.bundle.persistence.entity.BundleItem entity) {
                id = Objects.requireNonNull(entity.getId());
            } else {
                try {
                    var field = source.getClass().getDeclaredField("id");
                    field.setAccessible(true);
                    id = (long) field.get(source);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException("Failed to create provider for BundleExtend", e);
                }
            }

            return new io.theurl.bundle.domain.aggregate.BundleItem(id);
        };

        mapper.createTypeMap(io.theurl.bundle.persistence.entity.BundleItem.class, io.theurl.bundle.domain.aggregate.BundleItem.class)
              .setProvider(provider);
    }
}

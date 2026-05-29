package io.theurl.bundle.persistence.profile;

import io.theurl.bundle.domain.aggregate.BundleComment;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BundleCommentMapProfile {
    @Autowired
    private ModelMapper mapper;

    @PostConstruct
    public void configure() {
        Provider<BundleComment> provider = request -> {
            var source = request.getSource();

            Long id;

            if (source instanceof io.theurl.bundle.domain.aggregate.BundleComment entity) {
                id = entity.getId();
            } else {
                try {
                    var field = source.getClass().getDeclaredField("id");
                    field.setAccessible(true);
                    id = (Long) field.get(source);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException("Failed to create provider for BundleComment", e);
                }
            }

            return new io.theurl.bundle.domain.aggregate.BundleComment(id);
        };

        mapper.createTypeMap(io.theurl.bundle.persistence.entity.BundleComment.class, io.theurl.bundle.domain.aggregate.BundleComment.class)
              .setProvider(provider);
    }
}

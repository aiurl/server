package io.theurl.bundle.persistence.profile;

import io.theurl.bundle.persistence.entity.Bundle;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BundleMapProfile {
    @Autowired
    private ModelMapper mapper;

    @PostConstruct
    public void configure() {
        Provider<io.theurl.bundle.domain.aggregate.Bundle> provider = request -> {
            var source = request.getSource();

            Long id;

            if (source instanceof io.theurl.bundle.domain.aggregate.Bundle entity) {
                id = entity.getId();
            } else {
                try {
                    var field = source.getClass().getDeclaredField("id");
                    field.setAccessible(true);
                    id = (Long) field.get(source);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException("Failed to create provider for Bundle", e);
                }
            }

            return new io.theurl.bundle.domain.aggregate.Bundle(id);
        };

        mapper.createTypeMap(io.theurl.bundle.persistence.entity.Bundle.class, io.theurl.bundle.domain.aggregate.Bundle.class)
              .setProvider(provider)
              .addMappings(expression -> {
                  expression.map(Bundle::getType, (dest, value) -> setValue(dest, "type", value));
                  expression.map(Bundle::getVanity, (dest, value) -> setValue(dest, "vanity", value));
                  expression.map(Bundle::getName, io.theurl.bundle.domain.aggregate.Bundle::setName);
                  expression.map(Bundle::getDescription, io.theurl.bundle.domain.aggregate.Bundle::setDescription);
                  expression.map(Bundle::getImage, io.theurl.bundle.domain.aggregate.Bundle::setImage);
                  expression.map(Bundle::getOrder, io.theurl.bundle.domain.aggregate.Bundle::setOrder);
                  expression.map(Bundle::getItems, (dest, value) -> {
                      var items = dest.getItems();
                      items.add(mapper.map(value, io.theurl.bundle.domain.aggregate.BundleItem.class));
                  });
                  expression.map(Bundle::getComments, (dest, value) -> {
                      var comments = dest.getComments();
                      comments.add(mapper.map(value, io.theurl.bundle.domain.aggregate.BundleComment.class));
                  });
                  expression.map(Bundle::getExtend, (dest, value) -> {
                      var extend = (io.theurl.bundle.domain.aggregate.BundleExtend) value;
                      dest.getExtend().setItemCount(extend.getItemCount());
                      dest.getExtend().setCommentCount(extend.getCommentCount());
                      dest.getExtend().setFavoriteCount(extend.getFavoriteCount());
                      dest.getExtend().setFavoriteCount(extend.getFavoriteCount());
                      dest.getExtend().setLastVisitedAt(extend.getLastVisitedAt());
                  });
              });
    }

    /**
     * Set value to the field of the destination object using reflection.
     * This is necessary because some fields in the domain aggregate are not directly mapped from the entity, but need to be set manually after mapping.
     *
     * @param bundle the destination bundle object
     * @param name   the name of the field to set
     * @param value  the value to set
     */
    private void setValue(io.theurl.bundle.domain.aggregate.Bundle bundle, String name, Object value) {
        try {
            var field = bundle.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(bundle, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

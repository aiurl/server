package io.theurl.bundle.persistence.profile;

import io.theurl.bundle.persistence.entity.Bundle;
import io.theurl.bundle.persistence.model.BundleListModel;
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
//                  expression.map(Bundle::getType, (dest, value) -> setValue(dest, "type", value));
//                  expression.map(Bundle::getVanity, (dest, value) -> setValue(dest, "vanity", value));
//                  expression.map(Bundle::getOwnerId, (dest, value) -> setValue(dest, "ownerId", value));
//                  expression.map(Bundle::getOwnerName, (dest, value) -> setValue(dest, "ownerName", value));
                  expression.map(Bundle::getName, io.theurl.bundle.domain.aggregate.Bundle::setName);
                  expression.map(Bundle::getDescription, io.theurl.bundle.domain.aggregate.Bundle::setDescription);
                  expression.map(Bundle::getImage, io.theurl.bundle.domain.aggregate.Bundle::setImage);
                  expression.map(Bundle::getOrder, io.theurl.bundle.domain.aggregate.Bundle::setOrder);
                  // Extend fields are copied in setPostConverter below to avoid "Illegal DestinationSetter":
                  // BundleExtend (domain) has no no-arg constructor, so ModelMapper's proxy-based
                  // destination recorder cannot introspect nested paths like dest.getExtend().setXxx().
                  // No explicit skip needed: domain Bundle has no setExtend(), so ModelMapper ignores it.
              })
              .setPostConverter(ctx -> {
                  var src = ctx.getSource();
                  var dest = ctx.getDestination();
                  var extend = src.getExtend();
                  if (extend != null) {
                      dest.getExtend().setItemCount(extend.getItemsCount());
                      dest.getExtend().setFavoriteCount(extend.getFavoriteCount());
                      dest.getExtend().setCommentCount(extend.getCommentCount());
                      dest.getExtend().setVisitCount(extend.getVisitCount());
                      dest.getExtend().setLastVisitedAt(extend.getLastVisitedAt());
                  }
                  return dest;
              });

        mapper.createTypeMap(io.theurl.bundle.persistence.entity.Bundle.class, BundleListModel.class)
              .setPostConverter(ctx -> {
                  var src = ctx.getSource();
                  var dest = ctx.getDestination();
                  var extend = src.getExtend();
                  if (extend != null) {
                      dest.setItemsCount(extend.getItemsCount());
                      dest.setFavoriteCount(extend.getFavoriteCount());
                      dest.setCommentCount(extend.getCommentCount());
                      dest.setVisitCount(extend.getVisitCount());
                  }
                  return dest;
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
            if (value == null || bundle == null) {
                return;
            }
            var field = bundle.getClass().getSuperclass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(bundle, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

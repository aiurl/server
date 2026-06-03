package io.theurl.bundle.persistence.profile;

import io.theurl.bundle.persistence.entity.BundleItem;
import io.theurl.bundle.persistence.model.BundleListModel;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;

@Component
public class BundleMapProfile {
    @Autowired
    private ModelMapper mapper;

    @PostConstruct
    public void configure() {

        // entity.Bundle → domain.Bundle
        // Uses setConverter (not setProvider + addMappings) to prevent ModelMapper from
        // auto-mapping the 'extend' field via PRIVATE field access, which would try to
        // instantiate domain.BundleExtend — a class with no no-arg constructor.
        mapper.createTypeMap(io.theurl.bundle.persistence.entity.Bundle.class, io.theurl.bundle.domain.aggregate.Bundle.class)
              .setConverter(ctx -> {
                  var src = ctx.getSource();
                  assert src.getId() != null;
                  var dest = new io.theurl.bundle.domain.aggregate.Bundle(src.getId());
                  setValue(dest, "type", src.getType());
                  setValue(dest, "vanity", src.getVanity());
                  setValue(dest, "ownerId", src.getOwnerId());
                  setValue(dest, "ownerName", src.getOwnerName());
                  dest.setName(src.getName());
                  dest.setDescription(src.getDescription());
                  dest.setImage(src.getImage());
                  dest.setOrder(src.getOrder());
                  var extend = src.getExtend();
                  if (extend != null) {
                      dest.getExtend().setItemsCount(extend.getItemsCount());
                      dest.getExtend().setFavoriteCount(extend.getFavoriteCount());
                      dest.getExtend().setCommentCount(extend.getCommentCount());
                      dest.getExtend().setVisitCount(extend.getVisitCount());
                      dest.getExtend().setLastVisitedAt(extend.getLastVisitedAt());
                  }

                  setCollection(dest, "comments", src.getComments(), comment -> {
                      var destComment = new io.theurl.bundle.domain.aggregate.BundleComment(Objects.requireNonNull(comment.getId()));
                      destComment.setAuthorId(comment.getAuthorId());
                      destComment.setAuthorName(comment.getAuthorName());
                      destComment.setContent(comment.getContent());
                      destComment.setContact(comment.getContact());
                      destComment.setCreatedAt(comment.getCreatedAt());
                      return destComment;
                  });

                  setCollection(dest, "items", src.getItems(), item -> {
                      var destItem = new io.theurl.bundle.domain.aggregate.BundleItem(Objects.requireNonNull(item.getId()));
                      destItem.setUrl(item.getUrl());
                      destItem.setTitle(item.getTitle());
                      destItem.setDescription(item.getDescription());
                      destItem.setImage(item.getImage());
                      return destItem;
                  });
                  return dest;
              });

        // entity.Bundle → BundleListModel
        // Direct fields (id, type, vanity, name, …) are auto-mapped; extend fields
        // are copied in setPostConverter since BundleListModel has no 'extend' field
        // so ModelMapper never attempts to deep-map into it.
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

        mapper.createTypeMap(io.theurl.bundle.domain.aggregate.Bundle.class, io.theurl.bundle.persistence.entity.Bundle.class)
              .addMappings(expression -> {
                  expression.skip(io.theurl.bundle.persistence.entity.Bundle::setComments);
                  expression.skip(io.theurl.bundle.persistence.entity.Bundle::setItems);
              })
              .setPostConverter(ctx -> {
                  var src = ctx.getSource();
                  var dest = ctx.getDestination();
                  if (dest.getItems() == null) {
                      dest.setItems(new HashSet<>());
                  }
                  if (dest.getComments() == null) {
                      dest.setComments(new HashSet<>());
                  }
                  for (var item : src.getItems()) {
                      var destItem = dest.getItems().stream().filter(i -> {
                          assert i.getId() != null;
                          return i.getId().equals(item.getId());
                      }).findFirst().orElse(null);
                      if (destItem == null) {
                          destItem = new BundleItem();
                          destItem.setId(item.getId());
                          destItem.setBundleId(Objects.requireNonNull(dest.getId()));
                          dest.getItems().add(destItem);
                      }
                      destItem.setUrl(item.getUrl());
                      destItem.setTitle(item.getTitle());
                      destItem.setDescription(item.getDescription());
                      destItem.setImage(item.getImage());
                      destItem.setOrder(item.getOrder());
                  }
                  for (var comment : src.getComments()) {
                      var destComment = dest.getComments().stream().filter(c -> {
                          assert c.getId() != null;
                          return c.getId().equals(comment.getId());
                      }).findFirst().orElse(null);
                      if (destComment == null) {
                          destComment = new io.theurl.bundle.persistence.entity.BundleComment();
                          destComment.setId(comment.getId());
                          destComment.setAuthorId(comment.getAuthorId());
                          destComment.setAuthorName(comment.getAuthorName());
                          destComment.setContent(comment.getContent());
                          destComment.setContact(comment.getContact());
                          destComment.setCreatedAt(comment.getCreatedAt());
                          dest.getComments().add(destComment);
                      }
                  }
                  return dest;
              });
    }

    private void setValue(io.theurl.bundle.domain.aggregate.Bundle bundle, String name, Object value) {
        try {
            var field = io.theurl.bundle.domain.aggregate.Bundle.class.getDeclaredField(name);
            field.setAccessible(true);
            field.set(bundle, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }
    }

    private <S, D> void setCollection(io.theurl.bundle.domain.aggregate.Bundle bundle, String name, Collection<S> source, Function<S, D> convert) {
        try {
            var field = io.theurl.bundle.domain.aggregate.Bundle.class.getDeclaredField(name);
            field.setAccessible(true);
            var list = new ArrayList<D>();
            for (var item : source) {
                list.add(convert.apply(item));
            }
            field.set(bundle, list);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }
    }
}

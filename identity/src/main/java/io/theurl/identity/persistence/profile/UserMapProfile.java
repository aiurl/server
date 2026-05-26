package io.theurl.identity.persistence.profile;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapProfile {
    @Autowired
    private ModelMapper mapper;

    @PostConstruct
    public void configure() {
        Provider<io.theurl.identity.domain.aggregate.User> userProvider = request -> new io.theurl.identity.domain.aggregate.User(
            ((io.theurl.identity.persistence.entity.User) request.getSource()).getId()
        );
        Provider<io.theurl.identity.domain.aggregate.UserRole> userRoleProvider = request -> new io.theurl.identity.domain.aggregate.UserRole(
            ((io.theurl.identity.persistence.entity.UserRole) request.getSource()).getId(),
            ((io.theurl.identity.persistence.entity.UserRole) request.getSource()).getName()
        );
        Provider<io.theurl.identity.domain.aggregate.UserAuthority> userAuthorityProvider = request -> new io.theurl.identity.domain.aggregate.UserAuthority(
            ((io.theurl.identity.persistence.entity.UserAuthority) request.getSource()).getId(),
            ((io.theurl.identity.persistence.entity.UserAuthority) request.getSource()).getProvider(),
            ((io.theurl.identity.persistence.entity.UserAuthority) request.getSource()).getOpenId()
        );

        mapper.createTypeMap(io.theurl.identity.persistence.entity.UserRole.class, io.theurl.identity.domain.aggregate.UserRole.class)
              .setProvider(userRoleProvider);

        mapper.createTypeMap(io.theurl.identity.domain.aggregate.UserRole.class, io.theurl.identity.persistence.entity.UserRole.class)
              .addMappings(expression -> {
                  expression.map(io.theurl.identity.domain.aggregate.UserRole::getId, io.theurl.identity.persistence.entity.UserRole::setId);
                  expression.map(io.theurl.identity.domain.aggregate.UserRole::getName, io.theurl.identity.persistence.entity.UserRole::setName);
                  expression.skip(io.theurl.identity.persistence.entity.UserRole::setUserId);
              });

        mapper.createTypeMap(io.theurl.identity.persistence.entity.UserAuthority.class, io.theurl.identity.domain.aggregate.UserAuthority.class)
              .setProvider(userAuthorityProvider)
              .addMappings(expression -> {
                  expression.map(io.theurl.identity.persistence.entity.UserAuthority::getName, io.theurl.identity.domain.aggregate.UserAuthority::setName);
              });

        mapper.createTypeMap(io.theurl.identity.domain.aggregate.UserAuthority.class, io.theurl.identity.persistence.entity.UserAuthority.class)
              .addMappings(expression -> {
                  expression.map(io.theurl.identity.domain.aggregate.UserAuthority::getId, io.theurl.identity.persistence.entity.UserAuthority::setId);
                  expression.map(io.theurl.identity.domain.aggregate.UserAuthority::getProvider, io.theurl.identity.persistence.entity.UserAuthority::setProvider);
                  expression.map(io.theurl.identity.domain.aggregate.UserAuthority::getOpenId, io.theurl.identity.persistence.entity.UserAuthority::setOpenId);
                  expression.map(io.theurl.identity.domain.aggregate.UserAuthority::getName, io.theurl.identity.persistence.entity.UserAuthority::setName);
                  expression.skip(io.theurl.identity.persistence.entity.UserAuthority::setUserId);
              });

        mapper.createTypeMap(io.theurl.identity.persistence.entity.User.class, io.theurl.identity.domain.aggregate.User.class)
              .setProvider(userProvider)
              .addMappings(expression -> {
                  expression.map(io.theurl.identity.persistence.entity.User::getUsername, io.theurl.identity.domain.aggregate.User::setUsername);
                  expression.map(io.theurl.identity.persistence.entity.User::getNickname, io.theurl.identity.domain.aggregate.User::setNickname);
                  expression.map(io.theurl.identity.persistence.entity.User::getEmail, io.theurl.identity.domain.aggregate.User::setEmail);
                  expression.map(io.theurl.identity.persistence.entity.User::getPhone, io.theurl.identity.domain.aggregate.User::setPhone);
                  expression.map(io.theurl.identity.persistence.entity.User::getAccessFailedCount, io.theurl.identity.domain.aggregate.User::setAccessFailedCount);
                  expression.map(io.theurl.identity.persistence.entity.User::getLockedUntil, io.theurl.identity.domain.aggregate.User::setLockedUntil);
                  expression.map(io.theurl.identity.persistence.entity.User::getRoles, io.theurl.identity.domain.aggregate.User::setRoles);
              });

        mapper.createTypeMap(io.theurl.identity.domain.aggregate.User.class, io.theurl.identity.persistence.entity.User.class)
              .addMappings(expression -> {
                  expression.map(io.theurl.identity.domain.aggregate.User::getId, io.theurl.identity.persistence.entity.User::setId);
                  expression.map(io.theurl.identity.domain.aggregate.User::getUsername, io.theurl.identity.persistence.entity.User::setUsername);
                  expression.map(io.theurl.identity.domain.aggregate.User::getNickname, io.theurl.identity.persistence.entity.User::setNickname);
                  expression.map(io.theurl.identity.domain.aggregate.User::getEmail, io.theurl.identity.persistence.entity.User::setEmail);
                  expression.map(io.theurl.identity.domain.aggregate.User::getPhone, io.theurl.identity.persistence.entity.User::setPhone);
                  expression.map(io.theurl.identity.domain.aggregate.User::getAccessFailedCount, io.theurl.identity.persistence.entity.User::setAccessFailedCount);
                  expression.map(io.theurl.identity.domain.aggregate.User::getLockedUntil, io.theurl.identity.persistence.entity.User::setLockedUntil);
                  expression.map(io.theurl.identity.domain.aggregate.User::getRoles, io.theurl.identity.persistence.entity.User::setRoles);
              });

        mapper.createTypeMap(io.theurl.identity.persistence.entity.User.class, io.theurl.identity.persistence.model.UserDetail.class)
              .addMappings(expression -> {
                  expression.map(src -> src.getRoles() == null ? List.of() : src.getRoles().stream().map(io.theurl.identity.persistence.entity.UserRole::getName).toList(), io.theurl.identity.persistence.model.UserDetail::setRoles);
              });

    }
}

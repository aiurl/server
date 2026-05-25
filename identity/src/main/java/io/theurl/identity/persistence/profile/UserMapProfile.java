package io.theurl.identity.persistence.profile;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapProfile {
    @Autowired
    private ModelMapper mapper;

    @PostConstruct
    public void configure() {
        Provider<io.theurl.identity.domain.aggregate.User> userProvider = request -> new io.theurl.identity.domain.aggregate.User((Long) request.getSource());

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
    }
}

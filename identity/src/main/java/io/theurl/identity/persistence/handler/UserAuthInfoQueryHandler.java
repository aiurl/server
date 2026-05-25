package io.theurl.identity.persistence.handler;

import com.neroyun.mediator.Handler;
import io.theurl.identity.persistence.entity.User;
import io.theurl.identity.persistence.entity.UserRole;
import io.theurl.identity.persistence.model.UserAuthInfo;
import io.theurl.identity.persistence.query.UserAuthInfoQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserAuthInfoQueryHandler implements Handler<UserAuthInfoQuery, UserAuthInfo> {

    @PersistenceContext
    private EntityManager context;

    /**
     * Handles the given query and returns the corresponding UserAuthInfo.
     *
     * @param query the message to be processed by this handler
     * @return the UserAuthInfo corresponding to the given query
     */
    @Override
    @Async
    public CompletableFuture<UserAuthInfo> handleAsync(UserAuthInfoQuery query) {
        return supplyAsync(() -> {
            String sql = getSql(query);

            var typedQuery = context.createQuery(sql, User.class);

            switch (query.provider()) {
                case "id" -> typedQuery.setParameter("id", Long.parseLong(query.name()));
                case "email" -> typedQuery.setParameter("email", query.name());
                case "phone" -> typedQuery.setParameter("phone", query.name());
                case "username" -> typedQuery.setParameter("username", query.name());
                default -> {
                    typedQuery.setParameter("provider", query.provider());
                    typedQuery.setParameter("name", query.name());
                }
            }

            User user;

            try {
                user = typedQuery.getSingleResult();
            } catch (NoResultException | EntityNotFoundException _) {
                return null;
            }

            if (user == null) {
                return null;
            }

            List<UserRole> roles = context.createQuery("SELECT u from UserRole u where u.userId = :userId", UserRole.class).setParameter("userId", user.getId()).getResultList();

            return new UserAuthInfo() {{
                setId(user.getId());
                setUsername(user.getUsername());
                setPasswordHash(user.getPasswordHash());
                setPasswordSalt(user.getPasswordSalt());
                setLockedUntil(user.getLockedUntil());
                setNickname(user.getNickname());
                setEmail(user.getEmail());
                setPhone(user.getPhone());
                setRoles(roles.stream().map(UserRole::getName).toList());
            }};
        });
    }

    private static @NonNull String getSql(UserAuthInfoQuery query) {

        switch (query.provider()) {
            case "id" -> {
                return "SELECT u from User u where u.id = :id";
            }
            case "email" -> {
                return "SELECT u from User u where u.email = :email";
            }
            case "phone" -> {
                return "SELECT u from User u where u.phone = :phone";
            }
            case "username" -> {
                return "SELECT u from User u where u.username = :username";
            }
            default -> {
                return "SELECT u from User u where u.id in (SELECT ua.userId from UserAuthority ua where ua.provider = :provider and ua.openId = :name)";
            }
        }
    }
}

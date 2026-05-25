package io.theurl.identity.domain.repository;

import io.theurl.identity.domain.aggregate.User;

public interface UserRepository {
    void save(User user);

    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    User findByPhone(String phone);

    User findByAnyOf(String username, String email, String phone);
}

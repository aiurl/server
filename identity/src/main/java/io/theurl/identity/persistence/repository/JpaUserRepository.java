package io.theurl.identity.persistence.repository;

import io.theurl.identity.persistence.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email OR u.phone = :phone")
    Optional<User> findByAnyOf(String username, String email, String phone);
}

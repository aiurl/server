package io.theurl.identity.persistence.repository;

import io.theurl.framework.utility.Cryptography;
import io.theurl.framework.utility.RandomUtility;
import io.theurl.identity.domain.aggregate.User;
import io.theurl.identity.domain.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository repository;
    private final ModelMapper mapper;

    @PersistenceContext
    private EntityManager context;

    @Override
    public void save(User user) {
        var entity = repository.findById(user.getId())
                               .orElseGet(io.theurl.identity.persistence.entity.User::new);

        mapper.map(user, entity);

        if (user.getPassword() != null) {
            var salt = RandomUtility.randomString(32);
            try {
                var passwordHash = Cryptography.AES.encrypt(user.getPassword(), salt);
                entity.setPasswordHash(passwordHash);
                entity.setPasswordSalt(salt);
            } catch (Exception e) {
                throw new RuntimeException("Failed to encrypt password", e);
            }
        }
        repository.save(entity);
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id)
                         .map(entity -> mapper.map(entity, User.class))
                         .orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username)
                         .map(entity -> mapper.map(entity, User.class))
                         .orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email)
                         .map(entity -> mapper.map(entity, User.class))
                         .orElse(null);
    }

    @Override
    public User findByPhone(String phone) {
        return repository.findByPhone(phone)
                         .map(entity -> mapper.map(entity, User.class))
                         .orElse(null);
    }

    @Override
    public User findByAnyOf(String username, String email, String phone) {
        return repository.findByAnyOf(username, email, phone)
                         .map(entity -> mapper.map(entity, User.class))
                         .orElse(null);
    }
}

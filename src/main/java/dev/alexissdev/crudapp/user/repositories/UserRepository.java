package dev.alexissdev.crudapp.user.repositories;

import dev.alexissdev.crudapp.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Retrieves a user from the repository based on the provided username.
     *
     * @param username the username of the user to retrieve, must not be null
     * @return an Optional containing the user if found, or an empty Optional if no user exists with the given username
     */

    Optional<User> findByUsername(String username);

    /**
     * Checks if an entity with the specified ID exists in the repository.
     *
     * @param id the ID of the entity to check for existence
     * @return true if an entity with the given ID exists, false otherwise
     */

    boolean existsById(Long id);
}

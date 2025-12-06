package dev.alexissdev.crudapp.user.service;

import dev.alexissdev.crudapp.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * Retrieves a list of all User entities.
     *
     * @return a List containing all User entities
     */

    List<User> findAll();

    /**
     * Saves the provided User entity to the underlying data store.
     *
     * @param user the User entity to be saved
     * @return the saved User entity
     */

    User save(User user);

    /**
     * Finds a User entity by the provided username.
     *
     * @param username the username to search for
     * @return an Optional containing the User entity if found, or an empty Optional if no User is found
     */

    Optional<User> findByUsername(String username);

    /**
     * Finds a User entity by its unique identifier.
     *
     * @param id the unique identifier of the User to be retrieved
     * @return an Optional containing the User entity if found, or an empty Optional if no User is found
     */
    Optional<User> findById(Long id);

    /**
     * Deletes a User entity identified by its unique identifier.
     *
     * @param id the unique identifier of the User to be deleted
     * @return an Optional containing the deleted User entity if it existed and was deleted successfully,
     * or an empty Optional if no User with the given identifier was found
     */

    Optional<User> delete(Long id);

    /**
     * Deletes the provided User entity by delegating to the delete method that accepts a User's unique identifier.
     *
     * @param user the User entity to be deleted
     * @return an Optional containing the deleted User entity if it existed and was deleted successfully,
     * or an empty Optional if no User with the given identifier was found
     */

    default Optional<User> delete(User user) {
        return delete(user.getId());
    }
}

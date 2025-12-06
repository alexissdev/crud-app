package dev.alexissdev.crudapp.role.repositories;

import dev.alexissdev.crudapp.role.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository
        extends CrudRepository<Role, Long> {

    /**
     * Finds a Role entity by its name.
     *
     * @param name the name of the Role to search for
     * @return an Optional containing the Role if found, or an empty Optional if not found
     */

    Optional<Role> findByName(String name);
}

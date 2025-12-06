package dev.alexissdev.crudapp.product.repositories;

import dev.alexissdev.crudapp.product.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

    /**
     * Checks if a product with the specified name exists in the repository.
     *
     * @param name the name of the product to check for existence; must not be null or empty
     * @return true if a product with the specified name exists, otherwise false
     */

    boolean existsByName(String name);
}

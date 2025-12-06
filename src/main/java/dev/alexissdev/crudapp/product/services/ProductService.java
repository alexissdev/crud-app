package dev.alexissdev.crudapp.product.services;

import dev.alexissdev.crudapp.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    /**
     * Retrieves a list of all products.
     *
     * @return a list of all Product objects.
     */

    List<Product> findAll();

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the unique identifier of the product to be retrieved
     * @return an {@link Optional} containing the {@link Product} if found, or an empty {@link Optional} if not found
     */

    Optional<Product> findById(Long id);

    /**
     * Saves the given product entity to the repository.
     *
     * @param product the product entity to be saved
     * @return the saved product entity
     */

    Product save(Product product);

    /**
     * Updates the details of an existing product.
     * If the product with the given ID does not exist, an empty {@link Optional} is returned.
     *
     * @param product the product containing the updated details; must not be null and must contain a valid ID
     * @return an {@link Optional} containing the updated {@link Product}, or an empty {@link Optional} if the product does not exist
     */

    Optional<Product> update(Product product);

    /**
     * Deletes a product by its unique identifier.
     * If the product with the given ID exists, it is removed from the repository.
     *
     * @param id the unique identifier of the product to be deleted
     * @return an {@link Optional} containing the deleted {@link Product} if found and deleted,
     *         or an empty {@link Optional} if the product does not exist
     */

    Optional<Product> deleteById(Long id);

    /**
     * Deletes a specified product using its unique identifier obtained from the given product entity.
     * If the product with the corresponding ID exists, it is removed from the repository.
     *
     * @param product the product entity to be deleted; must not be null and must contain a valid ID
     * @return an {@link Optional} containing the deleted {@link Product} if found and deleted,
     *         or an empty {@link Optional} if the product does not exist
     */

    default Optional<Product> delete(Product product) {
        return deleteById(product.getId());
    }
}

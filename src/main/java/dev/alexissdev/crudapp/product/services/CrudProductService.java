package dev.alexissdev.crudapp.product.services;

import dev.alexissdev.crudapp.exception.EntityAlreadyExistsException;
import dev.alexissdev.crudapp.product.Product;
import dev.alexissdev.crudapp.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CrudProductService implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public CrudProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id)
                .or(Optional::empty);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new EntityAlreadyExistsException("There is already a product with the name: " + product.getName());
        }

        return productRepository.save(product);
    }

    @Override
    public Optional<Product> update(Product product) {
        if (product == null || product.getId() == null) {
            return Optional.empty();
        }

        return productRepository.findById(product.getId())
                .map(storageProduct -> {
                    storageProduct.setName(product.getName());
                    storageProduct.setPrice(product.getPrice());
                    storageProduct.setDescription(product.getDescription());
                    return productRepository.save(storageProduct);
                });
    }

    @Override
    @Transactional
    public Optional<Product> deleteById(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return product;
                });
    }

}

package dev.alexissdev.crudapp.product.services;

import dev.alexissdev.crudapp.exception.EntityAlreadyExistsException;
import dev.alexissdev.crudapp.product.Product;
import dev.alexissdev.crudapp.product.repositories.ProductRepository;
import dev.alexissdev.crudapp.redis.RedisConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CrudProductService
        implements ProductService {

    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redis;

    @Autowired
    public CrudProductService(ProductRepository productRepository, RedisTemplate<String, Object> redis) {
        this.productRepository = productRepository;
        this.redis = redis;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        String key = String.format(RedisConfiguration.PRODUCT_KEY, id);
        Product cachedProduct = (Product) redis.opsForValue().get(key);
        if (cachedProduct != null) {
            return Optional.of(cachedProduct);
        }

        return productRepository.findById(id)
                .map(product -> {
                    redis.opsForValue().set(key, product, 10, TimeUnit.MINUTES);
                    return product;
                })
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
                    redis.delete(String.format(RedisConfiguration.PRODUCT_KEY, storageProduct.getId()));

                    storageProduct.setName(product.getName());
                    storageProduct.setPrice(product.getPrice());
                    storageProduct.setDescription(product.getDescription());
                    return productRepository.save(storageProduct);
                });
    }

    @Override
    @Transactional
    public Optional<Product> deleteById(Long id) {
        redis.delete(String.format(RedisConfiguration.PRODUCT_KEY, id));
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return product;
                });
    }

}

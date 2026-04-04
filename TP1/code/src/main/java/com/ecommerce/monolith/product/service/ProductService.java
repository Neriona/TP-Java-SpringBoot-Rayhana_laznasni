package com.ecommerce.monolith.product.service;

import com.ecommerce.monolith.product.model.Product;
import com.ecommerce.monolith.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {

    private final ProductRepository repository;

    @Transactional(readOnly = true)
    public List<Product> getAll() {
        log.info("Fetching all products");
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getById(Long id) {
        log.info("Fetching product with id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }

    public Product create(Product product) {
        log.info("Creating new product: {}", product.getName());
        return repository.save(product);
    }

    public Product update(Long id, Product details) {
        log.info("Updating product with id: {}", id);
        Product product = getById(id);
        product.setName(details.getName());
        product.setDescription(details.getDescription());
        product.setPrice(details.getPrice());
        product.setStock(details.getStock());
        return repository.save(product);
    }

    public void delete(Long id) {
        log.info("Deleting product with id: {}", id);
        Product product = getById(id);
        repository.delete(product);
    }
}
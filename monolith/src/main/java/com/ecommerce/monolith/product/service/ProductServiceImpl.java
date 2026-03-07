package com.ecommerce.monolith.product.service;

import com.ecommerce.monolith.product.model.Product;
import com.ecommerce.monolith.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll();
    }
    @Override
    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }
    @Override
    public Product createProduct(Product product) {
        return repository.save(product);
    }
    @Override
    public Product updateProduct(Long id, Product newProduct) {
        Product prod = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        prod.setName(newProduct.getName());
        prod.setDescription(newProduct.getDescription());
        prod.setPrice(newProduct.getPrice());
        prod.setStock(newProduct.getStock());
        prod.setCategory(newProduct.getCategory());
        return repository.save(prod);
    }
    @Override
    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }
}
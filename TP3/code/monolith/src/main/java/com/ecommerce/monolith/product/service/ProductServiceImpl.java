package com.ecommerce.monolith.product.service;

import com.ecommerce.monolith.product.mapper.ProductMapper;
import com.ecommerce.monolith.product.dto.CreateProductRequest;
import com.ecommerce.monolith.product.dto.ProductDTO;
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
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        log.info("Fetching all products");
        return mapper.toDTOList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        Product product = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found with id: " + id));
        return mapper.toDTO(product);
    }

    @Override
    public ProductDTO createProduct(CreateProductRequest request) {
        log.info("Creating new product: {}", request.getName());
        Product product = mapper.toEntity(request);
        Product saved = repository.save(product);
        return mapper.toDTO(saved);
    }

    @Override
    public ProductDTO updateProduct(Long id, CreateProductRequest request) {
        log.info("Updating product with id: {}", id);
        Product product = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found with id: " + id));
        mapper.updateEntity(request, product);
        Product updated = repository.save(product);
        return mapper.toDTO(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Product not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
package com.ecommerce.productservice.service;

import com.ecommerce.productservice.exception.ResourceNotFoundException;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Récupérer tous les produits
     */
    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll();
    }

    /**
     * Récupérer un produit par ID
     */
    public Product getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);

        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + id));
    }

    /**
     * Créer un nouveau produit
     */
    public Product createProduct(Product product) {
        log.info("Creating new product: {}", product.getName());

        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }

        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }

        return productRepository.save(product);
    }

    /**
     * Mettre à jour un produit
     */
    public Product updateProduct(Long id, Product productDetails) {
        log.info("Updating product with ID: {}", id);

        Product product = getProductById(id);

        if (productDetails.getName() != null) {
            product.setName(productDetails.getName());
        }
        if (productDetails.getDescription() != null) {
            product.setDescription(productDetails.getDescription());
        }
        if (productDetails.getPrice() != null && productDetails.getPrice() > 0) {
            product.setPrice(productDetails.getPrice());
        }
        if (productDetails.getStock() != null && productDetails.getStock() >= 0) {
            product.setStock(productDetails.getStock());
        }

        return productRepository.save(product);
    }

    /**
     * Supprimer un produit
     */
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);

        Product product = getProductById(id);
        productRepository.delete(product);
    }

    /**
     * Vérifier le stock d'un produit
     */
    public boolean checkStock(Long id, int quantity) {
        log.info("Checking stock for product ID: {} with quantity: {}", id, quantity);

        Product product = getProductById(id);
        return product.getStock() >= quantity;
    }

    /**
     * Réduire le stock (pour les commandes)
     */
    public Product reduceStock(Long id, int quantity) {
        log.info("Reducing stock for product ID: {} by quantity: {}", id, quantity);

        Product product = getProductById(id);

        if (!checkStock(id, quantity)) {
            throw new IllegalArgumentException(
                    "Insufficient stock for product ID: " + id);
        }

        product.setStock(product.getStock() - quantity);
        return productRepository.save(product);
    }
}
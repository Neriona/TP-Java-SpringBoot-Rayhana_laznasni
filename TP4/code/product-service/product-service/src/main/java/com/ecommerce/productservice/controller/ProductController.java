package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Récupérer tous les produits
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("GET /api/products - Fetching all products");
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Récupérer un produit par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        log.info("GET /api/products/{} - Fetching product by ID", id);
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Créer un nouveau produit
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        log.info("POST /api/products - Creating new product: {}", product.getName());
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * Mettre à jour un produit
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product productDetails) {
        log.info("PUT /api/products/{} - Updating product", id);
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Supprimer un produit
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("DELETE /api/products/{} - Deleting product", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Vérifier le stock d'un produit
     */
    @GetMapping("/{id}/stock/{quantity}")
    public ResponseEntity<Boolean> checkStock(
            @PathVariable Long id,
            @PathVariable int quantity) {
        log.info("GET /api/products/{}/stock/{} - Checking stock", id, quantity);
        boolean hasStock = productService.checkStock(id, quantity);
        return ResponseEntity.ok(hasStock);
    }

    /**
     * Réduire le stock d'un produit
     */
    @PostMapping("/{id}/reduce-stock/{quantity}")
    public ResponseEntity<Product> reduceStock(
            @PathVariable Long id,
            @PathVariable int quantity) {
        log.info("POST /api/products/{}/reduce-stock/{} - Reducing stock", id, quantity);
        Product product = productService.reduceStock(id, quantity);
        return ResponseEntity.ok(product);
    }
}
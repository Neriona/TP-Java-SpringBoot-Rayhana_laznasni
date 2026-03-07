package com.ecommerce.monolith.category.controller;

import com.ecommerce.monolith.category.model.Category;
import com.ecommerce.monolith.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class categorycontroller {
    private final CategoryService service;

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(service.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        return service.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        Category created = service.createCategory(category);
        return ResponseEntity.status(201).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCategory(id);
        return ResponseEntity.noContent().build();//hello
    }
}
package com.ecommerce.monolith.category.service;

import com.ecommerce.monolith.category.model.Category;
import com.ecommerce.monolith.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Category createCategory(Category category) {
        if (repository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category already exists");
        }
        return repository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }
}
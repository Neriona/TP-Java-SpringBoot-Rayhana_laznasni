package com.ecommerce.monolith.category.service;

import com.ecommerce.monolith.category.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(Long id);
    Category createCategory(Category category);
    void deleteCategory(Long id);
}

package com.example.snkrs.service;

import com.example.snkrs.model.Category;
import com.example.snkrs.repository.CategoryRepository;
import com.example.snkrs.repository.ProductRepository;
import com.example.snkrs.request.SaveCategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.text.Normalizer;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Page<Category> getAllCategories(int page, int limit) {
        var pageable = PageRequest.of(page, limit);
        return categoryRepository.findAll(pageable);
    }

    public Category createCategory(SaveCategoryRequest request) {
        var category = new Category();
        category.setName(request.getName());
        // check slug is unique
        var slug = Normalizer.normalize(request.getSlug(), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9]", "-")
                .replaceAll("-+", "-")
                .toLowerCase();
        var existingCategory = categoryRepository.findBySlug(slug);
        if (existingCategory.isPresent()) {
            throw new RuntimeException("Slug already exists");
        }
        category.setSlug(slug);
        return categoryRepository.save(category);
    }

    public Category getCategory(String id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category updateCategory(String id, SaveCategoryRequest request) {
        var category = getCategory(id);
        category.setName(request.getName());
        // check slug is unique
        var slug = Normalizer.normalize(request.getSlug(), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9]", "-")
                .replaceAll("-+", "-")
                .toLowerCase();
        var existingCategory = categoryRepository.findBySlug(slug);
        if (existingCategory.isPresent() && !existingCategory.get().getId().equals(id)) {
            throw new RuntimeException("Slug already exists");
        }
        category.setSlug(slug);
        return categoryRepository.save(category);
    }

    public void deleteCategory(String id) {
        var category = getCategory(id);

        // remove category from products
        var products = productRepository.findByCategory(category);
        for (var product : products) {
            product.getCategories().remove(category);
            productRepository.save(product);
        }

        categoryRepository.delete(category);
    }
}

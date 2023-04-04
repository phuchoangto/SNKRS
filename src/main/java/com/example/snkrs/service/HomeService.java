package com.example.snkrs.service;

import com.example.snkrs.model.Category;
import com.example.snkrs.model.Product;
import com.example.snkrs.repository.CategoryRepository;
import com.example.snkrs.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public HomeService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<Product> getLatestProducts() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(0, 8, sort);
        return productRepository.findAll(pageRequest);
    }

    public Page<Product> getProducts(int page, int limit, String category, String search) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(page, limit, sort);
        boolean hasCategory = false;
        boolean hasSearch = search != null && !search.isBlank();


        Category categoryObj = new Category();


        if (!category.isBlank()) {
            categoryObj = categoryRepository.findByName(category);
            if (categoryObj != null) {
                hasCategory = true;
            }
        }

        if (hasCategory && hasSearch) {
            return productRepository.findAllByCategoriesAndNameContainingIgnoreCase(categoryObj, search, pageRequest);
        } else if (hasCategory) {
            return productRepository.findAllByCategories(categoryObj, pageRequest);
        } else if (hasSearch) {
            return productRepository.findAllByNameContainingIgnoreCase(search, pageRequest);
        } else {
            return productRepository.findAll(pageRequest);
        }
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}

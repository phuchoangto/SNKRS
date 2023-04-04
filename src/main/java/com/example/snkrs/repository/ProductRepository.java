package com.example.snkrs.repository;

import com.example.snkrs.model.Category;
import com.example.snkrs.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Page<Product> findAll(Pageable pageable);

    @Query("{ 'category': ?0 }")
    List<Product> findByCategory(Category category);

    @Query("{ 'slug': ?0 }")
    Optional<Product> findBySlug(String slug);

    Page<Product> findAllByCategoriesAndNameContainingIgnoreCase(Category category, String search, Pageable pageable);

    Page<Product> findAllByCategories(Category category, Pageable pageable);

    Page<Product> findAllByNameContainingIgnoreCase(String search, Pageable pageable);

}

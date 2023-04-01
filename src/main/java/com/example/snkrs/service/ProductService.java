package com.example.snkrs.service;

import com.example.snkrs.model.Product;
import com.example.snkrs.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getAllProducts(int page, int limit) {
        var pageable = PageRequest.of(page, limit);
        return productRepository.findAll(pageable);
    }
}

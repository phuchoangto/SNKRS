package com.example.snkrs.service;

import com.example.snkrs.model.Product;
import com.example.snkrs.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class HomeService {
    private final ProductRepository productRepository;

    @Autowired
    public HomeService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getLatestProducts() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(0, 8, sort);
        return productRepository.findAll(pageRequest);
    }
}

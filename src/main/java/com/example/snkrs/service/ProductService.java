package com.example.snkrs.service;

import com.example.snkrs.dto.ProductDTO;
import com.example.snkrs.model.Category;
import com.example.snkrs.model.Product;
import com.example.snkrs.model.ProductStatus;
import com.example.snkrs.model.Size;
import com.example.snkrs.repository.CategoryRepository;
import com.example.snkrs.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FirebaseStorageService firebaseStorageService;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, FirebaseStorageService firebaseStorageService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.firebaseStorageService = firebaseStorageService;
    }

    public Page<Product> getAllProducts(int page, int limit) {
        var pageable = PageRequest.of(page, limit);
        return productRepository.findAll(pageable);
    }

    public List<Size> getSizes(String id) {
        var product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return product.getSizes();
    }

    public Product getProduct(String id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product showProduct(String slug) {
        return productRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product createProduct(ProductDTO productDTO) {
        var product = new Product();
        product.setName(productDTO.getName());
        product.setSlug(productDTO.getSlug());
        product.setDescription(productDTO.getDescription());

        // check image is not null
        if (productDTO.getImage() == null) {
            throw new RuntimeException("Image is required");
        }

        // check image is not empty
        if (productDTO.getImage().isEmpty()) {
            throw new RuntimeException("Image is required");
        }

        // upload image to firebase
        String fileName = new Date().getTime() + "-" + productDTO.getImage().getOriginalFilename();
        try {
            firebaseStorageService.uploadFile(fileName, productDTO.getImage());
            product.setImage(firebaseStorageService.getFileUrl(fileName));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        // check valid category id
        List<Category> categories = new ArrayList<>();
        for (var categoryId : productDTO.getCategories()) {
            var category = categoryRepository.findById(categoryId);
            if (!category.isEmpty()) {
                categories.add(category.get());
            }
        }
        product.setCategories(categories);

        // json sizes
        Type listType = new TypeToken<List<Size>>(){}.getType();
        List<Size> sizes = new Gson().fromJson(productDTO.getSizes(), listType);
        product.setSizes(sizes);

        product.setStatus(productDTO.isPublish() ? ProductStatus.PUBLISHED : ProductStatus.UNPUBLISHED);
        return productRepository.save(product);
    }

    public Product updateProduct(String id, ProductDTO productDTO) {
        var product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new RuntimeException("Product not found");
        }
        var updatedProduct = product.get();
        updatedProduct.setName(productDTO.getName());
        updatedProduct.setSlug(productDTO.getSlug());
        updatedProduct.setDescription(productDTO.getDescription());
        updatedProduct.setStatus(productDTO.isPublish() ? ProductStatus.PUBLISHED : ProductStatus.UNPUBLISHED);

        // if image from dto is empty, use old image
        if (productDTO.getImage() != null && !productDTO.getImage().isEmpty()) {
            // upload image to firebase, filename = datetime+originalname
            var fileName = new Date().getTime() + "-" + productDTO.getImage().getOriginalFilename();
            try {
                firebaseStorageService.uploadFile(fileName, productDTO.getImage());
                updatedProduct.setImage(firebaseStorageService.getFileUrl(fileName));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        // check valid category id
        List<Category> categories = new ArrayList<>();
        for (var categoryId : productDTO.getCategories()) {
            var category = categoryRepository.findById(categoryId);
            if (!category.isEmpty()) {
                categories.add(category.get());
            }
        }
        updatedProduct.setCategories(categories);

        // json sizes
        Type listType = new TypeToken<List<Size>>(){}.getType();
        List<Size> sizes = new Gson().fromJson(productDTO.getSizes(), listType);
        updatedProduct.setSizes(sizes);

        updatedProduct.setStatus(productDTO.isPublish() ? ProductStatus.PUBLISHED : ProductStatus.UNPUBLISHED);
        return productRepository.save(updatedProduct);
    }

    public Product togglePublishProduct(String id) {
        var product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new RuntimeException("Product not found");
        }
        var updatedProduct = product.get();
        updatedProduct.setStatus(updatedProduct.getStatus() == ProductStatus.PUBLISHED ? ProductStatus.UNPUBLISHED : ProductStatus.PUBLISHED);
        return productRepository.save(updatedProduct);
    }
}

package com.example.snkrs.controller.api;

import com.example.snkrs.common.ApiResponse;
import com.example.snkrs.dto.ProductDTO;
import com.example.snkrs.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductApiController {
    private final ProductService productService;

    @Autowired
    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse> createProduct(@Valid ProductDTO productDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid request", bindingResult.getAllErrors()));
        }
        try {
            var createdProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(new ApiResponse("Product created successfully", createdProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/api/products/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable String id, @Valid ProductDTO productDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid request", bindingResult.getAllErrors()));
        }
        try {
            var updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(new ApiResponse("Product updated successfully", updatedProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/api/products/{id}/toggle-publish")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse> togglePublishProduct(@PathVariable String id) {
        try {
            var product = productService.togglePublishProduct(id);
            return ResponseEntity.ok(new ApiResponse("Product updated successfully", product));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/api/products/{id}/sizes")
    public ResponseEntity<ApiResponse> getSizes(@PathVariable String id) {
        try {
            var sizes = productService.getSizes(id);
            return ResponseEntity.ok(new ApiResponse("Sizes fetched successfully", sizes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

}

package com.example.snkrs.controller.api;

import com.example.snkrs.common.ApiResponse;
import com.example.snkrs.request.SaveCategoryRequest;
import com.example.snkrs.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryApiController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryApiController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/api/categories")
    public ResponseEntity<ApiResponse> createCategory(@Valid SaveCategoryRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid request", bindingResult.getAllErrors()));
        }
        try {
            var category = categoryService.createCategory(request);
            return ResponseEntity.ok(new ApiResponse("Category created successfully", category));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/api/categories/{id}")
    public ResponseEntity<ApiResponse> getCategory(@PathVariable String id) {
        try {
            var category = categoryService.getCategory(id);
            return ResponseEntity.ok(new ApiResponse("Category fetched successfully", category));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/api/categories/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable String id, @Valid SaveCategoryRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid request", bindingResult.getAllErrors()));
        }
        try {
            var category = categoryService.updateCategory(id, request);
            return ResponseEntity.ok(new ApiResponse("Category updated successfully", category));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/api/categories/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse("Category deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }
}

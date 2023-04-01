package com.example.snkrs.controller;

import com.example.snkrs.model.Category;
import com.example.snkrs.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/dashboard/categories")
    public String manageCategories(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int limit,
                                   Model model) {
        var categories = categoryService.getAllCategories(page, limit);
        model.addAttribute("categories", categories);
        return "category/list";
    }
}

package com.example.snkrs.controller;

import com.example.snkrs.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/dashboard/categories")
    public String manageCategories(Model model) {
        var categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "category/list";
    }
}

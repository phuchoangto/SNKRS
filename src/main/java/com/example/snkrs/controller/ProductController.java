package com.example.snkrs.controller;

import com.example.snkrs.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/dashboard/products")
    public String manageProducts(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int limit,
                                 Model model) {
        var products = productService.getAllProducts(page, limit);
        model.addAttribute("products", products);
        return "product/list";
    }

    @GetMapping("/dashboard/products/create")
    public String createProduct() {
        return "product/create";
    }

    @GetMapping("/dashboard/products/{id}")
    public String editProduct(@PathVariable String id, Model model) {
        try {
            var product = productService.getProduct(id);
            model.addAttribute("product", product);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "product/create";
    }

    @GetMapping("/products/{slug}")
    public String product(@PathVariable String slug, Model model) {
        try {
            var product = productService.showProduct(slug);
            model.addAttribute("product", product);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "product/detail";
    }
}

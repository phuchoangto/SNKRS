package com.example.snkrs.controller;

import com.example.snkrs.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/")
    public String home(Model model) {
        var latestProducts = homeService.getLatestProducts();
        model.addAttribute("latestProducts", latestProducts);
        return "home";
    }

    @GetMapping("/shop")
    public String shop(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "20") int limit,
                       @RequestParam(defaultValue = "createdAt") String sortBy,
                       @RequestParam(defaultValue = "", required = false) String category,
                       @RequestParam(defaultValue = "", required = false) String search,
                       Model model) {
        var products = homeService.getProducts(page, limit, category, search);
        var categories = homeService.getCategories();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "shop";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}

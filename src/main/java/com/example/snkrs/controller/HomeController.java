package com.example.snkrs.controller;

import com.example.snkrs.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        return "dashboard";
    }
}

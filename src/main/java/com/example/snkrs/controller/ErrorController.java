package com.example.snkrs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/404")
    public String notFound() {
        return "error/404";
    }
}

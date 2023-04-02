package com.example.snkrs.controller;

import com.example.snkrs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard/users")
    public String manageUser(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int limit,
                             Model model) {
        try {
            var users = userService.getAllUsers(page, limit);
            model.addAttribute("users", users);
            return "user/list";
        } catch (Exception e) {
            return "redirect:/404";
        }
    }
}

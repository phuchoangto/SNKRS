package com.example.snkrs.controller.api;

import com.example.snkrs.common.ApiResponse;
import com.example.snkrs.request.SaveUserRequest;
import com.example.snkrs.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {
    private final UserService userService;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/api/users/{id}/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> updateRoles(@PathVariable String id, String roles) {
        try {
            var user = userService.updateRoles(id, roles);
            return ResponseEntity.ok(new ApiResponse("Roles updated successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/api/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> saveUser(@Valid SaveUserRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse(bindingResult.getAllErrors().get(0).getDefaultMessage(), null));
        }
        try {
            var user = userService.saveUser(request);
            return ResponseEntity.ok(new ApiResponse("User created successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }
}

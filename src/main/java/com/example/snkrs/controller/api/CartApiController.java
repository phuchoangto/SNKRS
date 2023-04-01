package com.example.snkrs.controller.api;

import com.example.snkrs.common.ApiResponse;
import com.example.snkrs.model.CartItem;
import com.example.snkrs.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@SessionAttributes("cart")
public class CartApiController {
    private final CartService cartService;

    @Autowired
    public CartApiController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/api/cart")
    public ResponseEntity<ApiResponse> addToCart(@Valid CartItem cartItem, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid request", bindingResult.getAllErrors()));
        }
        try {
            var cart = cartService.addItemToCart(cartItem);
            return ResponseEntity.ok(new ApiResponse("Add to cart successfully", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/api/cart")
    public ResponseEntity<ApiResponse> getCart() {
        try {
            return ResponseEntity.ok(new ApiResponse("Fetch cart successfully", cartService.getCart()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/api/cart")
    public ResponseEntity<ApiResponse> updateCart(@Valid CartItem cartItem, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid request", bindingResult.getAllErrors()));
        }
        try {
            var cart = cartService.updateItemInCart(cartItem);
            return ResponseEntity.ok(new ApiResponse("Update cart successfully", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/api/cart")
    public ResponseEntity<ApiResponse> deleteItemInCart(@Valid CartItem cartItem, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid request", bindingResult.getAllErrors()));
        }
        try {
            var cart = cartService.deleteItemInCart(cartItem);
            return ResponseEntity.ok(new ApiResponse("Delete item in cart successfully", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }
}

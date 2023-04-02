package com.example.snkrs.controller.api;

import com.example.snkrs.common.ApiResponse;
import com.example.snkrs.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderApiController {
    private final OrderService orderService;

    @Autowired
    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PutMapping("/api/orders/{id}/status")
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable String id, String status) {
        try {
            var order = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(new ApiResponse("Update order status successfully", order));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/api/orders/{id}")
    public ResponseEntity<ApiResponse> getOrder(@PathVariable String id) {
        try {
            var order = orderService.getOrderById(id);
            return ResponseEntity.ok(new ApiResponse("Get order successfully", order));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }
}

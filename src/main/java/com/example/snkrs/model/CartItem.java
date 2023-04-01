package com.example.snkrs.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItem {
    @NotBlank
    private String id;
    @NotBlank
    private String size;
    @NotNull
    private Integer quantity;
    private Product product;
    private Size sizeObj;
}

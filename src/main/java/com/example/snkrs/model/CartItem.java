package com.example.snkrs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;

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

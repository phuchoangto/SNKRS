package com.example.snkrs.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Size {
    private String name;
    private Long quantity;
    private BigDecimal price;
}

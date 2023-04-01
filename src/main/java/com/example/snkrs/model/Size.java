package com.example.snkrs.model;

import com.example.snkrs.common.CurrencyFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode
public class Size {
    private String name;
    private Long quantity;
    private BigDecimal price;

    public String getFormattedPrice() {
        // 3.000.000 USD
        return CurrencyFormatter.format(price);
    }
}

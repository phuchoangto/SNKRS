package com.example.snkrs.model;

import com.example.snkrs.common.CurrencyFormatter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cart implements Serializable {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        items.add(item);
    }

    public BigDecimal getTotalPrice() {
        var totalPrice = BigDecimal.ZERO;
        for (var item : items) {
            totalPrice = totalPrice.add(item.getSizeObj().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return totalPrice;
    }

    @JsonProperty("totalPrice")
    public String getFormattedTotalPrice() {
        return CurrencyFormatter.format(getTotalPrice());
    }

    @JsonProperty("totalItems")
    public int getTotalItems() {
        return items.size();
    }
}

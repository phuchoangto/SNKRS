package com.example.snkrs.model;

import com.example.snkrs.common.CurrencyFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;

    @Indexed(unique = true)
    private String slug;

    private String description;
    private String image;
    private List<Category> categories;
    private List<Size> sizes;
    private ProductStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public String getDisplayPrice() {
        // if has 1 size, return formatted price with comma each 3 digits
        if (sizes.size() == 1) {
            return CurrencyFormatter.format(sizes.get(0).getPrice());
        }
        // if has more than 1 size, return formatted price range
        BigDecimal minPrice = sizes.get(0).getPrice();
        BigDecimal maxPrice = sizes.get(0).getPrice();
        for (Size size : sizes) {
            if (size.getPrice().compareTo(minPrice) < 0) {
                minPrice = size.getPrice();
            }
            if (size.getPrice().compareTo(maxPrice) > 0) {
                maxPrice = size.getPrice();
            }
        }
        // if min price and max price are the same, return formatted price
        if (minPrice.compareTo(maxPrice) == 0) {
            return CurrencyFormatter.format(minPrice);
        }
        // if min price and max price are different, return formatted price range
        return CurrencyFormatter.formatRange(minPrice, maxPrice);
    }
}

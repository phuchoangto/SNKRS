package com.example.snkrs.model;

import com.example.snkrs.common.CurrencyFormatter;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private User user;
    private List<Map<String, Object>> items;
    private PaymentMethod paymentMethod;
    private OrderStatus status = OrderStatus.NEW;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String note;
    private String address;
    private BigDecimal total;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public String getFormattedTotal() {
        return CurrencyFormatter.format(total);
    }

    public String getFormattedCreatedAt() {
        return createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getFormattedUpdatedAt() {
        return updatedAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getHiddenEmail() {
        // eg: m***h@gmail.com
        return email.replaceAll("(?<=.{2}).(?=.*@)", "*");
    }

    public String getHiddenPhone() {
        // replace first 6 digits with *
        return phone.replaceAll("^\\d{6}", "******");
    }

    public String getHiddenAddress() {
        // split with comma and remove the first element
        String[] addressParts = address.split(",");
        String[] hiddenAddressParts = new String[0];
        if (addressParts.length > 1) {
            hiddenAddressParts = new String[addressParts.length - 1];
            System.arraycopy(addressParts, 1, hiddenAddressParts, 0, addressParts.length - 1);
        }
        return String.join(",", hiddenAddressParts);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}

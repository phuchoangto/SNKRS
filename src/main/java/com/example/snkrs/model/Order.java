package com.example.snkrs.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private OrderStatus status = OrderStatus.WAITING_FOR_PAYMENT;
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
}

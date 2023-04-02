package com.example.snkrs.service;

import com.example.snkrs.model.*;
import com.example.snkrs.repository.OrderRepository;
import com.example.snkrs.repository.ProductRepository;
import com.example.snkrs.request.SaveOrderRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SessionScope
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final AuthService authService;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CartService cartService, AuthService authService,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.authService = authService;
        this.productRepository = productRepository;
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Page<Order> getAllOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size));
    }

    public Order completeOrder(Order order) {
        order.setStatus(OrderStatus.PROCESSING);
        return orderRepository.save(order);
    }

    public Order createOrder(SaveOrderRequest request) {
        // check if cart is empty
        if (cartService.getCart().getItems().size() == 0) {
            throw new RuntimeException("Cart is empty");
        }

        // create order
        Order order = new Order();
        order.setUser(authService.getCurrentUser());
        order.setFirstName(request.getFirstName());
        order.setLastName(request.getLastName());
        order.setEmail(request.getEmail());
        order.setPhone(request.getPhone());
        order.setNote(request.getNote());
        order.setPaymentMethod(request.getPaymentMethod());
        String fullAddress = request.getAddress();

        RestTemplate restTemplate = new RestTemplate();

        // call api: https://provinces.open-api.vn/api/p/{id} to get province name
        String provinceName = restTemplate.getForObject("https://provinces.open-api.vn/api/p/" + request.getProvince(), ProvinceResponse.class).getName();
        String districtName = restTemplate.getForObject("https://provinces.open-api.vn/api/d/" + request.getDistrict(), DistrictResponse.class).getName();
        String wardName = restTemplate.getForObject("https://provinces.open-api.vn/api/w/" + request.getWard(), WardResponse.class).getName();

        fullAddress = fullAddress + ", " + wardName + ", " + districtName + ", " + provinceName;
        order.setAddress(fullAddress);
        order.setPhone(request.getPhone());

        // set cart items to order
        List<Map<String, Object>> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartService.getCart().getItems()) {
            Map<String, Object> itemMap = new HashMap<>();
            var product = productRepository.findById(item.getId()).orElseThrow(() -> new RuntimeException("Product not found"));
            var size = product.getSizes().stream().filter(s -> s.getName().equals(item.getSize())).findFirst().orElseThrow(() -> new RuntimeException("Size not found"));
            itemMap.put("product", product);
            itemMap.put("size", size);
            itemMap.put("quantity", item.getQuantity());
            // decrease quantity of product
            size.setQuantity(size.getQuantity() - item.getQuantity());
            // save
            productRepository.save(product);
            items.add(itemMap);
            total = total.add(size.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        order.setItems(items);
        order.setTotal(total);

        // if payment method is CASH, set status to PROCESSING
        if (request.getPaymentMethod() == PaymentMethod.COD) {
            order.setStatus(OrderStatus.PROCESSING);
        }

        // if payment method is PAYPAL, set status to NEW
        if (request.getPaymentMethod() == PaymentMethod.PAYPAL) {
            order.setStatus(OrderStatus.NEW);
        }

        // if request.saveInfo is true, save user info
        if (request.getSaveInfo()) {
            Address address = new Address();
            address.setFirstName(request.getFirstName());
            address.setLastName(request.getLastName());
            address.setPhone(request.getPhone());
            address.setAddress(request.getAddress());
            address.setProvince(request.getProvince());
            address.setDistrict(request.getDistrict());
            address.setWard(request.getWard());
            User user = authService.getCurrentUser();
            user.setAddress(address);
        }

        // save order
        return orderRepository.save(order);
    }

    public Order updateOrderStatus(String id, String status) {
        Order order = getOrderById(id);
        order.setStatus(OrderStatus.valueOf(status));
        return orderRepository.save(order);
    }
}

@Data
class ProvinceResponse {
    private String name;
    private int code;
    private String division_type;
    private String codename;
    private int phone_code;
}

@Data
class DistrictResponse {
    private String name;
    private int code;
    private String division_type;
    private String codename;
    private int province_code;
}

@Data
class WardResponse {
    private String name;
    private int code;
    private String division_type;
    private String codename;
    private int district_code;
}
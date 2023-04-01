package com.example.snkrs.service;

import com.example.snkrs.model.Cart;
import com.example.snkrs.model.CartItem;
import com.example.snkrs.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;

@Service
@SessionScope
public class CartService {
    private final ProductRepository productRepository;

    private Cart cart = new Cart();

    @Autowired
    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<CartItem> showCart() {
        var items = cart.getItems();
        for (var item : items) {
            var product = productRepository.findById(item.getId()).orElseThrow(() -> new RuntimeException("Product not found"));
            var size = product.getSizes().stream().filter(s -> s.getName().equals(item.getSize())).findFirst().get();
            item.setProduct(product);
            item.setSizeObj(size);
        }
        return items;
    }

    public void addItemToCart(CartItem item) {
        // check product exist
        var product = productRepository.findById(item.getId()).orElseThrow(() -> new RuntimeException("Product not found"));
        // check size exist
        var sizeExist = product.getSizes().stream().anyMatch(s -> s.getName().equals(item.getSize()));
        if (!sizeExist) {
            throw new RuntimeException("Size not found");
        }
        // check quantity is valid
        if (item.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }
        // check quantity is in stock
        var sizeInStock = product.getSizes().stream().filter(s -> s.getName().equals(item.getSize())).findFirst().get();
        if (item.getQuantity() > sizeInStock.getQuantity()) {
            throw new RuntimeException("Quantity is out of stock");
        }
        // check item is already in cart
        var itemExist = cart.getItems().stream().anyMatch(i -> i.getId().equals(item.getId()) && i.getSize().equals(item.getSize()));
        if (itemExist) {
            throw new RuntimeException("Item is already in cart");
        }
        // add item to cart
        cart.getItems().add(item);
    }

    public void removeItemFromCart(CartItem item) {
        cart.getItems().remove(item);
    }

    public void clearCart() {
        cart.getItems().clear();
    }

    public Cart getCart() {
        return cart;
    }
}

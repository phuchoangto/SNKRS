package com.example.snkrs.controller;

import com.example.snkrs.common.CurrencyConverter;
import com.example.snkrs.common.UrlUtil;
import com.example.snkrs.config.PaypalPaymentIntent;
import com.example.snkrs.config.PaypalPaymentMethod;
import com.example.snkrs.request.SaveOrderRequest;
import com.example.snkrs.service.CartService;
import com.example.snkrs.service.OrderService;
import com.example.snkrs.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@SessionAttributes("cart")
public class OrderController {
    private final CartService cartService;
    private final OrderService orderService;
    private final PaypalService paypalService;
    private final CurrencyConverter currencyConverter;

    @Autowired
    public OrderController(CartService cartService, OrderService orderService, PaypalService paypalService, CurrencyConverter currencyConverter) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.paypalService = paypalService;
        this.currencyConverter = currencyConverter;
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        try {
            model.addAttribute("cart", cartService.showCart());
            model.addAttribute("saveOrderRequest", new SaveOrderRequest());
            return "checkout";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/checkout")
    public String checkout(HttpServletRequest request, @Valid @ModelAttribute SaveOrderRequest saveOrderRequest, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("cart", cartService.showCart());
            return "checkout";
        }

        try {
            var order = orderService.createOrder(saveOrderRequest);
            try {
                double total = currencyConverter.convertVNDToUSD(order.getTotal().doubleValue());
                Payment payment = paypalService.createPayment(
                    total,
                    "USD",
                    PaypalPaymentMethod.PAYPAL,
                    PaypalPaymentIntent.SALE,
                    "Payment for order " + order.getId(),
            UrlUtil.getBaseURL(request) + "/orders/" + order.getId(),
            UrlUtil.getBaseURL(request) + "/orders/" + order.getId()
                );
                for(Links links : payment.getLinks()){
                    if(links.getRel().equals("approval_url")){
                        return "redirect:" + links.getHref();
                    }
                }
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
                return "checkout";
            }
            model.addAttribute("order", order);
            return "checkout";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "checkout";
        }
    }

    @GetMapping("/orders/{id}")
    public String completeOrder(HttpServletRequest request, @PathVariable String id, Model model) {
        try {
            var order = orderService.getOrderById(id);
            var paymentId = request.getParameter("paymentId");
            var payerId = request.getParameter("PayerID");

            if (paymentId != null && payerId != null) {
                Payment payment = paypalService.executePayment(request.getParameter("paymentId"), request.getParameter("PayerID"));
                if(payment.getState().equals("approved")){
                    var orderCompleted = orderService.completeOrder(order);
                    model.addAttribute("order", orderCompleted);
                    return "order/completed";
                } else {
                    model.addAttribute("error", "Payment failed");
                    return "order/completed";
                }
            }
            model.addAttribute("order", order);
            return "order/completed";
        } catch (Exception e) {
                return "redirect:/404";
        }
    }

    @GetMapping("/dashboard/orders")
    public String manageOrders(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int limit,
                               Model model) {
        try {
            model.addAttribute("orders", orderService.getAllOrders(page, limit));
            return "order/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/dashboard/orders/{id}")
    public String manageOrder(@PathVariable String id, Model model) {
        try {
            model.addAttribute("order", orderService.getOrderById(id));
            return "order/detail";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/dashboard/orders/{id}/delivery")
    public String deliveryOrder(@PathVariable String id, Model model) {
        try {
            model.addAttribute("order", orderService.getOrderById(id));
            return "order/delivery";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}

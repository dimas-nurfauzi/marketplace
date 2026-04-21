package com.dims.marketplace.service.impl;

import com.dims.marketplace.dto.cart.response.CheckoutResponse;
import com.dims.marketplace.dto.enums.OrderStatus;
import com.dims.marketplace.dto.mapper.OrderMapper;
import com.dims.marketplace.dto.order.response.OrderResponse;
import com.dims.marketplace.entity.*;
import com.dims.marketplace.repository.CartRepository;
import com.dims.marketplace.repository.OrderRepository;
import com.dims.marketplace.service.inter.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    @Override
    public CheckoutResponse placeOrder(UUID userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            Variant v = cartItem.getVariant();

            if (cartItem.getQuantity() > v.getStock()) {
                throw new RuntimeException("Stock not enough");
            }

            BigDecimal price = v.getPrice();
            int qty = cartItem.getQuantity();
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(qty));

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setVariant(v);
            oi.setProductName(v.getProduct().getName());
            oi.setVariantName(v.getSize());
            oi.setPrice(price);
            oi.setQuantity(qty);
            oi.setSubtotal(subtotal);

            orderItems.add(oi);
            total = total.add(subtotal);

            // reduce stock
            v.setStock(v.getStock() - qty);
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);

        orderRepository.save(order);

        // clear cart
        cart.getItems().clear();

        return new CheckoutResponse(order.getId(), total, order.getStatus().name());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(UUID userId) {

        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return orders.stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderDetail(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return OrderMapper.toResponse(order);
    }
}
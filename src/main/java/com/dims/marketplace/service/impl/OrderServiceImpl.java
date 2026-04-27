package com.dims.marketplace.service.impl;

import com.dims.marketplace.dto.cart.response.CheckoutResponse;
import com.dims.marketplace.dto.enums.OrderStatus;
import com.dims.marketplace.dto.mapper.OrderMapper;
import com.dims.marketplace.dto.order.response.OrderResponse;
import com.dims.marketplace.entity.*;
import com.dims.marketplace.exceptions.BadRequestException;
import com.dims.marketplace.exceptions.DataNotFoundException;
import com.dims.marketplace.exceptions.UnauthorizedException;
import com.dims.marketplace.repository.CartRepository;
import com.dims.marketplace.repository.OrderRepository;
import com.dims.marketplace.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Override
    public CheckoutResponse placeOrder(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new DataNotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            Variant v = cartItem.getVariant();

            if (cartItem.getQuantity() > v.getStock()) {
                throw new BadRequestException("Stock not enough");
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

            v.setStock(v.getStock() - qty);
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);

        orderRepository.save(order);

        cart.getItems().clear();

        return new CheckoutResponse(order.getId(), total, order.getStatus().name());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        return orders.stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderDetail(UUID orderId, String email) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));

        // VALIDASI OWNER
        if (!order.getUser().getEmail().equals(email)) {
            throw new UnauthorizedException("You don't have access to this order");
        }

        return OrderMapper.toResponse(order);
    }
}
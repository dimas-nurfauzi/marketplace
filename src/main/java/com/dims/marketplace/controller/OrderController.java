package com.dims.marketplace.controller;

import com.dims.marketplace.dto.ApiResponse;
import com.dims.marketplace.dto.cart.response.CheckoutResponse;
import com.dims.marketplace.dto.order.response.OrderResponse;
import com.dims.marketplace.service.inter.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    @PreAuthorize("hasAuthority('BUYER')")
    public ResponseEntity<ApiResponse<CheckoutResponse>> placeOrder(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Order created",
                        orderService.placeOrder(email))
        );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('BUYER')")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrders(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Success",
                        orderService.getUserOrders(email))
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BUYER')")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderDetail(
            @PathVariable UUID id,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Success",
                        orderService.getOrderDetail(id, email))
        );
    }
}
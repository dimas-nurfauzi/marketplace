package com.dims.marketplace.controller;

import com.dims.marketplace.dto.ApiResponse;
import com.dims.marketplace.dto.cart.response.CheckoutResponse;
import com.dims.marketplace.dto.order.response.OrderResponse;
import com.dims.marketplace.service.inter.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<ApiResponse<CheckoutResponse>> placeOrder(
            @RequestParam UUID userId) {

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Order created", orderService.placeOrder(userId))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrders(
            @RequestParam UUID userId) {

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Success",
                        orderService.getUserOrders(userId))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderDetail(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Success",
                        orderService.getOrderDetail(id))
        );
    }
}
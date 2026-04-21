package com.dims.marketplace.dto.mapper;

import com.dims.marketplace.dto.order.response.OrderItemResponse;
import com.dims.marketplace.dto.order.response.OrderResponse;
import com.dims.marketplace.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

public class OrderMapper {

    public static OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items = order.getItems().stream()
                .map(i -> new OrderItemResponse(
                        i.getProductName(),
                        i.getVariantName(),
                        i.getPrice(),
                        i.getQuantity(),
                        i.getSubtotal()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getTotalPrice(),
                order.getStatus().name(),
                order.getCreatedAt(),
                items
        );
    }
}
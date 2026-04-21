package com.dims.marketplace.dto.order.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderItemResponse {
    private String productName;
    private String variantName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}
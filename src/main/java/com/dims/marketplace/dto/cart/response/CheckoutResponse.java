package com.dims.marketplace.dto.cart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CheckoutResponse {
    private UUID orderId;
    private BigDecimal totalPrice;
    private String status;
}
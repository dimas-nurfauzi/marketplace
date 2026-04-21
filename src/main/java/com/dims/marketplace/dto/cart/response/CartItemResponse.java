package com.dims.marketplace.dto.cart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CartItemResponse {
    private UUID itemId;
    private UUID variantId;
    private String productName;
    private String variantName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}

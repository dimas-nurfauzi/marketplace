package com.dims.marketplace.dto.cart.response;

import com.dims.marketplace.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CartResponse {
    private UUID cartId;
    private UUID userId;
    private List<CartItemResponse> items;
    private BigDecimal totalPrice;
}